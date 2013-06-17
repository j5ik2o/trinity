package org.sisioh.trinity.test

import org.sisioh.trinity.domain.{ScalatraLikeController, Config}
import org.sisioh.trinity.view.ScalateView
import org.sisioh.trinity.application.TrinityApplication
import org.specs2.mutable.Specification


class TestController(application: TrinityApplication) extends ScalatraLikeController(application) {

  get("/hey") {
    request => responseBuilder.withPlain("hello").withOk.toFuture
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
      responseBuilder.withBody(ScalateView("scalate_test.ssp", bindings)).toFuture
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


}

class ServerSpec extends Specification {

  val config = Config()

  "app" should {
    "register" in {
      val s = TrinityApplication(config)
      val controller = new TestController(s)
      s.registerController(controller)
      s.start()

      Thread.sleep(5 * 60000)
    }
  }

}
