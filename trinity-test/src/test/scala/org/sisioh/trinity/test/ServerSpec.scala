package org.sisioh.trinity.test

import org.sisioh.trinity.application.TrinityApplication
import org.specs2.mutable.Specification
import org.sisioh.trinity.domain.controller.SimpleController
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.view.scalate.{ScalateEngineContext, ScalateRenderer}


class TestController(implicit application: TrinityApplication) extends SimpleController {

  get("/hey/hello") {
    request =>
      responseBuilder.withPlain("hello2").withOk.toTrinityResponseFuture
  }

  get("/hey/:userId") {
    request =>
      responseBuilder.withPlain("hello1 = "+request.routeParams("userId")).withOk.toTrinityResponseFuture
  }




  get("/scalate") {
    request =>
      val bindings = Map(
        "name" -> "Scalate",
        "languages" -> List("Java", "Scala", "Clojure", "Groovy")
      )
      implicit val scalate = ScalateEngineContext()
      responseBuilder.withBodyRenderer(ScalateRenderer("scalate_test.ssp", bindings)).toTrinityResponseFuture
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


}

//class ServerSpec extends Specification {
//
//  val config = Config()
//
//  "app" should {
//    "register" in {
//      implicit val s = TrinityApplication(config)
////      val controller = new TestController
////     s.registerController(controller)
////      s.start()
//    }
//  }
//
//}
