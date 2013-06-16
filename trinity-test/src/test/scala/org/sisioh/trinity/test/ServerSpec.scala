package org.sisioh.trinity.test

import org.sisioh.trinity.{Controller, TrinityServer, Config}
import org.sisioh.trinity.view.ScalateView


class TestController(config: Config) extends Controller(config) {

  get("/hey") {
    request => render.withPlain("hello").withOk.toFuture
  }

  //  class TestView extends MustacheView {
  //    def template: String = "test_view.mustache"
  //
  //    val test_val = "aaaa"
  //  }


  //  get("/test") {
  //    request =>
  //      render.withView(new TestView).toFuture
  //  }

  get("/scalate") {
    request =>
      val bindings = Map(
        "name" -> "Scalate",
        "languages" -> List("Java", "Scala", "Clojure", "Groovy")
      )
      render.withView(ScalateView(config, "scalate_test.ssp", bindings)).toFuture
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


}

class ServerSpec extends SpecHelper {

  val config = Config()

  def controller = new TestController(config)

  "app" should {
    "register" in {
      val s = TrinityServer(config)
      s.registerController(controller)
      s.start()

      Thread.sleep(5 * 60000)
    }
  }

}
