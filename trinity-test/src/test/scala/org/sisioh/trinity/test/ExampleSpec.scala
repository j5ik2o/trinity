package org.sisioh.trinity.test

import com.twitter.ostrich.stats.Stats
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.controller.{GlobalSettings, SimpleController}
import org.sisioh.trinity.domain.http.{TrinityResponseBuilder, ContentType}
import org.sisioh.trinity.domain.routing.FutureAction
import org.sisioh.trinity.view.scalate.{ScalateEngineContext, ScalateRenderer}
import org.specs2.mutable.Specification

class ExampleSpec
  extends Specification
  with ControllerUnitTestSupport {

  class UnauthorizedException extends Exception

  implicit val application = MockApplication(
    MockConfig(
      localDocumentRoot = "trinity-test/src/test/resources"
    )
  )

  implicit val config = application.config

  implicit object ExampleController extends SimpleController {

    /**
     * Basic Example
     *
     * curl http://localhost:7070/hello => "hello world"
     */
    get("/hello") {
      request =>
        responseBuilder.withPlain("hello world").toTrinityResponseFuture
    }

    /**
     * Route parameters
     *
     * curl http://localhost:7070/user/dave => "hello dave"
     */
    get("/user/:username") {
      request =>
        val username = request.routeParams.getOrElse("username", "default_user")
        responseBuilder.withPlain("hello " + username).toTrinityResponseFuture
    }

    /**
     * Setting Headers
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/headers") {
      request =>
        responseBuilder.withPlain("look at headers").withHeader("Foo", "Bar").toTrinityResponseFuture
    }

    /**
     * Rendering json
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/data.json") {
      request =>
        import org.json4s.JsonDSL._
        responseBuilder.withJson(Map("foo" -> "bar")).toTrinityResponseFuture
    }

    /**
     * Query params
     *
     * curl http://localhost:7070/search?q=foo => "no results for foo"
     */
    get("/search") {
      request =>
        request.params.get("q").map {
          q =>
            responseBuilder.withPlain("no results for " + q).toFinagleResponseFuture
        }.getOrElse {
          responseBuilder.withPlain("query param q needed").withStatus(500).toFinagleResponseFuture
        }
    }

    /**
     * Uploading files
     *
     * curl -F avatar=@/path/to/img http://localhost:7070/profile
     */
    post("/profile") {
      request =>
        request.multiParams.get("avatar").map {
          avatar =>
            println("content type is " + avatar.contentType)
            avatar.writeToFile("/tmp/avatar") //writes uploaded avatar to /tmp/avatar
        }
        responseBuilder.withPlain("ok").toTrinityResponseFuture
    }

    get("/template") {
      request =>
        implicit val scalate = ScalateEngineContext()
        val view = ScalateRenderer("scalate.mustache", Map("message" -> "aaaa"))
        responseBuilder.withBodyRenderer(view).toTrinityResponseFuture
    }


    /**
     * Custom Error Handling
     *
     * curl http://localhost:7070/error
     */
    get("/error") {
      request =>
        1234 / 0
        responseBuilder.withPlain("we never make it here").toTrinityResponseFuture
    }

    /**
     * Custom Error Handling with custom Exception
     *
     * curl http://localhost:7070/unautorized
     */
    get("/unauthorized") {
      request =>
        throw new UnauthorizedException
    }

    /**
     * Dispatch based on Content-Type
     *
     * curl http://localhost:7070/index.json
     * curl http://localhost:7070/index.html
     */
    get("/blog/index.:format") {
      request =>
        import org.json4s.JsonDSL._
        respondTo(request) {
          case ContentType.TextHtml => responseBuilder.withHtml("<h1>Hello</h1>").toTrinityResponseFuture
          case ContentType.AppJson => responseBuilder.withJson(Map("value" -> "hello")).toTrinityResponseFuture
        }
    }

    /**
     * Also works without :format route using browser Accept header
     *
     * curl -H "Accept: text/html" http://localhost:7070/another/page
     * curl -H "Accept: application/json" http://localhost:7070/another/page
     * curl -H "Accept: foo/bar" http://localhost:7070/another/page
     */
    get("/another/page") {
      request =>
        respondTo(request) {
          case ContentType.TextHtml =>
            responseBuilder.withPlain("an html response").toTrinityResponseFuture
          case ContentType.AppJson =>
            responseBuilder.withPlain("an json response").toTrinityResponseFuture
          case ContentType.All =>
            responseBuilder.withPlain("default fallback response").toTrinityResponseFuture
        }
    }

    /**
     * Metrics are supported out of the box via Twitter's Ostrich library.
     * More details here: https://github.com/twitter/ostrich
     *
     * curl http://localhost:7070/slow_thing
     *
     * By default a stats server is started on 9990:
     *
     * curl http://localhost:9990/stats.txt
     *
     */
    get("/slow_thing") {
      request =>
        Stats.incr("slow_thing")
        Stats.time("slow_thing time") {
          Thread.sleep(100)
        }
        responseBuilder.withPlain("slow").toTrinityResponseFuture
    }

  }


  override val getGlobalSettings = Some(
    new GlobalSettings with LoggingEx {

      override def notFound = Some(
        FutureAction {
          request =>
            TrinityResponseBuilder().withStatus(404).withPlain("not found yo").toTrinityResponseFuture
        }
      )

      override def error = Some(
        FutureAction {
          request =>
            request.error match {
              case Some(e: ArithmeticException) =>
                TrinityResponseBuilder().withStatus(500).withPlain("whoops, divide by zero!").toTrinityResponseFuture
              case Some(e: UnauthorizedException) =>
                TrinityResponseBuilder().withStatus(401).withPlain("Not Authorized!").toTrinityResponseFuture
              case Some(ex) =>
                TrinityResponseBuilder().withStatus(415).withPlain(ex.toString).toTrinityResponseFuture
              case _ =>
                TrinityResponseBuilder().withStatus(500).withPlain("Something went wrong!").toTrinityResponseFuture
            }
        }
      )
    }
  )

  "GET /notfound" should {
    "respond 404" in {
      testGet("/notfound") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== "not found yo"
              response.code must_== 404
          }
      }
    }
  }

  "GET /error" should {
    "respond 500" in {
      testGet("/error") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== "whoops, divide by zero!"
              response.code must_== 500
          }
      }
    }
  }

  "GET /unauthorized" should {
    "respond 401" in {
      testGet("/unauthorized") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== "Not Authorized!"
              response.code must_== 401
          }
      }
    }
  }

  "GET /hello" should {
    "respond with hello world" in {
      testGet("/hello") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== "hello world"
          }
      }
    }
  }

  "GET /user/foo" should {
    "responsd with hello foo" in {
      testGet("/user/foo") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== "hello foo"
          }
      }
    }
  }

  "GET /headers" should {
    "respond with Foo:Bar" in {
      testGet("/headers") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.getHeader("Foo") must_== "Bar"
          }
      }
    }
  }

  "GET /data.json" should {
    """respond with {"foo":"bar"}""" in {
      testGet("/data.json") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== """{"foo":"bar"}"""
          }
      }
    }
  }

  "GET /search?q=foo" should {
    "respond with no results for foo" in {
      testGet("/search?q=foo") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== "no results for foo"
          }
      }
    }
  }

  "GET /template" should {
    "respond with a rendered template" in {
      testGet("/template") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body.trim must_== "aaaa"
          }
      }
    }
  }

  "GET /blog/index.json" should {
    "should have json" in {
      testGet("/blog/index.json") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== """{"value":"hello"}"""
          }
      }
    }
  }

  "GET /blog/index.html" should {
    "should have html" in {
      testGet("/blog/index.html") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== """<h1>Hello</h1>"""
          }
      }
    }
  }

  "GET /blog/index.rss" should {
    "respond in a 415" in {
      testGet("/blog/index.rss") {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.code must_== 415
          }
      }
    }
  }

  "GET /another/page with html" should {
    "respond with html" in {
      testGet("/another/page", None, Map("Accept" -> "text/html")) {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== "an html response"
          }
      }
    }
  }

  "GET /another/page with json" should {
    "respond with json" in {
      testGet("/another/page", None, Map("Accept" -> "application/json")) {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== "an json response"
          }
      }
    }
  }

  "GET /another/page with unsupported type" should {
    "respond with catch all" in {
      testGet("/another/page", None, Map("Accept" -> "foo/bar")) {
        responseTry =>
          responseTry must beSuccessfulTry.like {
            case response =>
              response.body must_== "default fallback response"
          }
      }
    }
  }

}
