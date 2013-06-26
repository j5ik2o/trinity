package org.sisioh.trinity.test

import com.twitter.finagle.http.Response
import com.twitter.ostrich.stats.Stats
import com.twitter.util.Future
import scala.Some
import org.sisioh.trinity.domain.controller.{GlobalSettings, SimpleController}
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.http.{ResponseBuilder, Request, ContentType}
import org.sisioh.trinity.view.scalate.{ScalateEngineContext, ScalateRenderer}
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import org.specs2.mutable.Specification

class ExampleSpec extends Specification with ControllerTestSupport {

  /**
   * Custom Error Handling with custom Exception
   *
   * curl http://localhost:7070/unautorized
   */
  class UnauthorizedException extends Exception

  /* ###BEGIN_APP### */

  implicit val application = new MockApplication(Config())

  object ExampleController extends SimpleController {

    /**
     * Basic Example
     *
     * curl http://localhost:7070/hello => "hello world"
     */
    get("/hello") {
      request =>
        responseBuilder.withPlain("hello world").toFuture
    }

    /**
     * Route parameters
     *
     * curl http://localhost:7070/user/dave => "hello dave"
     */
    get("/user/:username") {
      request =>
        val username = request.routeParams.getOrElse("username", "default_user")
        responseBuilder.withPlain("hello " + username).toFuture
    }

    /**
     * Setting Headers
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/headers") {
      request =>
        responseBuilder.withPlain("look at headers").withHeader("Foo", "Bar").toFuture
    }

    /**
     * Rendering json
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/data.json") {
      request =>
        import org.json4s.JsonDSL._
        responseBuilder.withJson(Map("foo" -> "bar")).toFuture
    }

    /**
     * Query params
     *
     * curl http://localhost:7070/search?q=foo => "no results for foo"
     */
    get("/search") {
      request =>
        request.params.get("q") match {
          case Some(q) => responseBuilder.withPlain("no results for " + q).toFuture
          case None => responseBuilder.withPlain("query param q needed").withStatus(HttpResponseStatus.valueOf(500)).toFuture
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
        responseBuilder.withPlain("ok").toFuture
    }

    get("/template") {
      request =>
        implicit val scalate = ScalateEngineContext()
        val view = ScalateRenderer("test_view.mustache", Map("test_val" -> "aaaa"))
        responseBuilder.withBodyRenderer(view).toFuture
    }


    /**
     * Custom Error Handling
     *
     * curl http://localhost:7070/error
     */
    get("/error") {
      request =>
        1234 / 0
        responseBuilder.withPlain("we never make it here").toFuture
    }


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
          case ContentType.TextHtml => responseBuilder.withHtml("<h1>Hello</h1>").toFuture
          case ContentType.AppJson => responseBuilder.withJson(Map("value" -> "hello")).toFuture
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
          case ContentType.TextHtml => responseBuilder.withPlain("an html response").toFuture
          case ContentType.AppJson => responseBuilder.withPlain("an json response").toFuture
          case ContentType.All => responseBuilder.withPlain("default fallback response").toFuture
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
        responseBuilder.withPlain("slow").toFuture
    }

  }

  val controller = ExampleController

  override val globalSetting = Some(new GlobalSettings {
    def notFound(request: Request): Future[Response] = {
      ResponseBuilder().withStatus(HttpResponseStatus.valueOf(404)).withPlain("not found yo").toFuture
    }

    def error(request: Request): Future[Response] = {
      request.error match {
        case Some(e: ArithmeticException) =>
          ResponseBuilder().withStatus(HttpResponseStatus.valueOf(500)).withPlain("whoops, divide by zero!").toFuture
        case Some(e: UnauthorizedException) =>
          ResponseBuilder().withStatus(HttpResponseStatus.valueOf(401)).withPlain("Not Authorized!").toFuture
        case Some(ex) =>
          ResponseBuilder().withStatus(HttpResponseStatus.valueOf(415)).withPlain("Unsupported Media Type!").toFuture
        case _ =>
          ResponseBuilder().withStatus(HttpResponseStatus.valueOf(500)).withPlain("Something went wrong!").toFuture
      }
    }
  })

  /* ###END_APP### */


  /* ###BEGIN_SPEC### */

  "GET /notfound" should {
    "respond 404" in {
      testGet("/notfound") {
        response =>
          response.body must_== ("not found yo")
          response.code must_== (404)
      }
    }
  }

  "GET /error" should {
    "respond 500" in {
      testGet("/error") {
        response =>
          response.body must_== ("whoops, divide by zero!")
          response.code must_== (500)
      }
    }
  }

  "GET /unauthorized" should {
    "respond 401" in {
      testGet("/unauthorized") {
        response =>
          response.body must_== ("Not Authorized!")
          response.code must_== (401)
      }
    }
  }

  "GET /hello" should {
    "respond with hello world" in {
      testGet("/hello") {
        response =>
          response.body must_== ("hello world")
      }
    }
  }

  "GET /user/foo" should {
    "responsd with hello foo" in {
      testGet("/user/foo") {
        response =>
          response.body must_== ("hello foo")
      }
    }
  }

  "GET /headers" should {
    "respond with Foo:Bar" in {
      testGet("/headers") {
        response =>
          response.getHeader("Foo") must_== ("Bar")
      }
    }
  }

  "GET /data.json" should {
    """respond with {"foo":"bar"}""" in {
      testGet("/data.json") {
        response =>
          response.body must_== ("""{"foo":"bar"}""")
      }
    }
  }

  "GET /search?q=foo" should {
    "respond with no results for foo" in {
      testGet("/search?q=foo") {
        response =>
          response.body must_== ("no results for foo")
      }
    }
  }

  "GET /template" should {
    "respond with a rendered template" in {
      testGet("/template") {
        response =>
          response.body must_== ("aaaa")
      }
    }
  }

  "GET /blog/index.json" should {
    "should have json" in {
      testGet("/blog/index.json") {
        response =>
          response.body must_== ("""{"value":"hello"}""")
      }
    }
  }

  "GET /blog/index.html" should {
    "should have html" in {
      testGet("/blog/index.html") {
        response =>
          response.body must_== ("""<h1>Hello</h1>""")
      }
    }
  }

  "GET /blog/index.rss" should {
    "respond in a 415" in {
      testGet("/blog/index.rss") {
        response =>
          response.code must_== (415)
      }
    }
  }

  "GET /another/page with html" should {
    "respond with html" in {
      testGet("/another/page", Map.empty, Map("Accept" -> "text/html")) {
        response =>
          response.body must_== ("an html response")
      }
    }
  }

  "GET /another/page with json" should {
    "respond with json" in {
      testGet("/another/page", Map.empty, Map("Accept" -> "application/json")) {
        response =>
          response.body must_== ("an json response")
      }
    }
  }

  "GET /another/page with unsupported type" should {
    "respond with catch all" in {
      testGet("/another/page", Map.empty, Map("Accept" -> "foo/bar")) {
        response =>
          response.body must_== ("default fallback response")
      }
    }
  }

  /* ###END_SPEC### */
}
