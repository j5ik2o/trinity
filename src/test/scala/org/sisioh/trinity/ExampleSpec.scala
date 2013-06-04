package org.sisioh.trinity

import com.twitter.ostrich.stats.Stats
import com.twitter.util.Future

class ExampleSpec extends SpecHelper {

  sequential

  /**
   * Custom Error Handling with custom Exception
   *
   * curl http://localhost:7070/unautorized
   */
  class UnauthorizedException extends Exception

  /* ###BEGIN_APP### */

  class ExampleApp extends Controller {

    /**
     * Basic Example
     *
     * curl http://localhost:7070/hello => "hello world"
     */
    get("/hello") {
      request =>
        render.plain("hello world").toFuture
    }

    /**
     * Route parameters
     *
     * curl http://localhost:7070/user/dave => "hello dave"
     */
    get("/user/:username") {
      request =>
        val username = request.routeParams.getOrElse("username", "default_user")
        render.plain("hello " + username).toFuture
    }

    /**
     * Setting Headers
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/headers") {
      request =>
        render.plain("look at headers").header("Foo", "Bar").toFuture
    }

    /**
     * Rendering json
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/data.json") {
      request =>
        render.json(Map("foo" -> "bar")).toFuture
    }

    /**
     * Query params
     *
     * curl http://localhost:7070/search?q=foo => "no results for foo"
     */
    get("/search") {
      request =>
        request.params.get("q") match {
          case Some(q) => render.plain("no results for " + q).toFuture
          case None => render.plain("query param q needed").status(500).toFuture
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
        render.plain("ok").toFuture
    }

    /**
     * Rendering views
     *
     * curl http://localhost:7070/posts
     */
    class AnView extends View {
      val template = "an_view.mustache"
      val some_val = "random value here"
    }

    get("/template") {
      request =>
        val anView = new AnView
        render.view(anView).toFuture
    }


    /**
     * Custom Error Handling
     *
     * curl http://localhost:7070/error
     */
    get("/error") {
      request =>
        1234 / 0
        render.plain("we never make it here").toFuture
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
        respondTo(request) {
          case ContentType.TextHtml => render.html("<h1>Hello</h1>").toFuture
          case ContentType.AppJson => render.json(Map("value" -> "hello")).toFuture
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
          case ContentType.TextHtml => render.plain("an html response").toFuture
          case ContentType.AppJson => render.plain("an json response").toFuture
          case ContentType.All => render.plain("default fallback response").toFuture
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
        render.plain("slow").toFuture
    }

  }

  val app = new ExampleApp

  override val globalSetting = Some(new GlobalSetting {
    def notFound(request: Request): Future[Response] = {
      new Response().status(404).plain("not found yo").toFuture
    }

    def error(request: Request): Future[Response] = {
      request.error match {
        case Some(e: ArithmeticException) =>
          new Response().status(500).plain("whoops, divide by zero!").toFuture
        case Some(e: UnauthorizedException) =>
          new Response().status(401).plain("Not Authorized!").toFuture
        case Some(ex) =>
          new Response().status(415).plain("Unsupported Media Type!").toFuture
        case _ =>
          new Response().status(500).plain("Something went wrong!").toFuture
      }
    }
  })

  /* ###END_APP### */


  /* ###BEGIN_SPEC### */

  "GET /notfound" should {
    "respond 404" in {
      get("/notfound")
      response.body must_== ("not found yo")
      response.code must_== (404)
    }
  }

  "GET /error" should {
    "respond 500" in {
      get("/error")
      response.body must_== ("whoops, divide by zero!")
      response.code must_== (500)
    }
  }

  "GET /unauthorized" should {
    "respond 401" in {
      get("/unauthorized")
      response.body must_== ("Not Authorized!")
      response.code must_== (401)
    }
  }

  "GET /hello" should {
    "respond with hello world" in {
      get("/hello")
      response.body must_== ("hello world")
    }
  }

  "GET /user/foo" should {
    "responsd with hello foo" in {
      get("/user/foo")
      response.body must_== ("hello foo")
    }
  }

  "GET /headers" should {
    "respond with Foo:Bar" in {
      get("/headers")
      response.getHeader("Foo") must_== ("Bar")
    }
  }

  "GET /data.json" should { """respond with {"foo":"bar"}""" in {
    get("/data.json")
    response.body must_== ("""{"foo":"bar"}""")
  }
  }

  "GET /search?q=foo" should {
    "respond with no results for foo" in {
      get("/search?q=foo")
      response.body must_== ("no results for foo")
    }
  }

  "GET /template" should {
    "respond with a rendered template" in {
      get("/template")
      response.body must_== ("Your value is random value here")
    }
  }

  "GET /blog/index.json" should {
    "should have json" in {
      get("/blog/index.json")
      response.body must_== ("""{"value":"hello"}""")
    }
  }

  "GET /blog/index.html" should {
    "should have html" in {
      get("/blog/index.html")
      response.body must_== ("""<h1>Hello</h1>""")
    }
  }

  "GET /blog/index.rss" should {
    "respond in a 415" in {
      get("/blog/index.rss")
      response.code must_== (415)
    }
  }

  "GET /another/page with html" should {
    "respond with html" in {
      get("/another/page", Map.empty, Map("Accept" -> "text/html"))
      response.body must_== ("an html response")
    }
  }

  "GET /another/page with json" should {
    "respond with json" in {
      get("/another/page", Map.empty, Map("Accept" -> "application/json"))
      response.body must_== ("an json response")
    }
  }

  "GET /another/page with unsupported type" should {
    "respond with catch all" in {
      get("/another/page", Map.empty, Map("Accept" -> "foo/bar"))
      response.body must_== ("default fallback response")
    }
  }

  /* ###END_SPEC### */
}
