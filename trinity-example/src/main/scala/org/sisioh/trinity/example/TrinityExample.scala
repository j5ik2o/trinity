package org.sisioh.trinity.example

import com.twitter.ostrich.stats.Stats
import com.twitter.util.Future
import org.sisioh.trinity._
import scala.Some
import org.sisioh.trinity.view.ScalateView

object TrinityExample {

  class ExampleController(config: Config) extends Controller(config) {

    /**
     * Basic Example
     *
     * curl http://localhost:7070/hello => "hello world"
     */
    get("/") {
      request =>
        render.withPlain("hello world").toFuture
    }

    /**
     * Route parameters
     *
     * curl http://localhost:7070/user/dave => "hello dave"
     */
    get("/user/:username") {
      request =>
        val username = request.routeParams.getOrElse("username", "default_user")
        render.withPlain("hello " + username).toFuture
    }

    /**
     * Setting Headers
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/headers") {
      request =>
        render.withPlain("look at headers").withHeader("Foo", "Bar").toFuture
    }

    /**
     * Rendering json
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/data.json") {
      request =>
        import org.json4s.JsonDSL._
        render.withJson(Map("foo" -> "bar")).toFuture
    }

    /**
     * Query params
     *
     * curl http://localhost:7070/search?q=foo => "no results for foo"
     */
    get("/search") {
      request =>
        request.params.get("q") match {
          case Some(q) => render.withPlain("no results for " + q).toFuture
          case None => render.withPlain("query param q needed").withStatus(500).toFuture
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
        render.withPlain("ok").toFuture
    }

    get("/template") {
      request =>
       render.withBody(ScalateView(config, "template.mustache", Map("some_val" -> "random value here"))).toFuture
    }


    /**
     * Custom Error Handling
     *
     * curl http://localhost:7070/error
     */
    get("/error") {
      request =>
        1234 / 0
        render.withPlain("we never make it here").toFuture
    }

    /**
     * Custom Error Handling with custom Exception
     *
     * curl http://localhost:7070/unautorized
     */
    class UnauthorizedException extends Exception

    get("/unauthorized") {
      request =>
        throw new UnauthorizedException
    }


    val globalSettings = new GlobalSetting {
      def error(request: RequestAdaptor): Future[ResponseBuilder] = {
        request.error match {
          case Some(e: ArithmeticException) =>
            render.withStatus(500).withPlain("whoops, divide by zero!").toFuture
          case Some(e: UnauthorizedException) =>
            render.withStatus(401).withPlain("Not Authorized!").toFuture
          case Some(e) =>
            render.withStatus(415).withPlain("Unsupported Media Type!").toFuture
          case _ =>
            render.withStatus(500).withPlain("Something went wrong!").toFuture
        }
      }

      def notFound(request: RequestAdaptor): Future[ResponseBuilder] = {
        render.withStatus(404).withPlain("not found yo").toFuture
      }

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
          case ContentType.TextHtml => render.withHtml("<h1>Hello</h1>").toFuture
          case ContentType.AppJson => render.withJson(Map("value" -> "hello")).toFuture
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
          case ContentType.TextHtml => render.withPlain("an html response").toFuture
          case ContentType.AppJson => render.withPlain("an json response").toFuture
          case ContentType.All => render.withPlain("default fallback response").toFuture
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
        render.withPlain("slow").toFuture
    }

  }


  def main(args: Array[String]) = {
    val config = Config()
    val app = new ExampleController(config)
    val server = TrinityServer(config, Some(app.globalSettings))
    server.registerController(app)
    server.start()
  }


}
