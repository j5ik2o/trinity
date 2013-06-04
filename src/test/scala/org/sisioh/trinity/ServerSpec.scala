package org.sisioh.trinity


class TestApp extends Controller {

  get("/hey") {
    request => render.plain("hello").ok.toFuture
  }

  class TestView extends MustacheView {
    def template: String = "test_view.mustache"

    val test_val = "aaaa"
  }


  get("/test") {
    request =>
      render.view(new TestView).toFuture
  }

  get("/scalate") {
    request =>
      val bindings = Map(
        "name" -> "Scalate",
        "languages" -> List("Java", "Scala", "Clojure", "Groovy")
      )
      render.view(new ScalateView("scalate_test.ssp", bindings)).toFuture
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


}

class ServerSpec extends SpecHelper {

  def app = {
    new TestApp
  }

  "app" should {
    "register" in {
      TrinityServer.register(app)
      TrinityServer.start()

      Thread.sleep(5 * 60000)
    }
  }

}
