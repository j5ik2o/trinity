package org.sisioh.trinity.test

import org.specs2.mutable.Specification
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.controller.SimpleController

class ControllerUnitTestSupportSpec
  extends Specification
  with ControllerUnitTestSupport {

  case class CookieTestController()(implicit application: TrinityApplication)
    extends SimpleController {

    post("/test1") {
      request =>
        responseBuilder.withPlain("content = " + request.contentString).toFinagleResponseFuture
    }

    post("/test2") {
      request =>
        responseBuilder.withPlain("content = " + request.params("test")).toFinagleResponseFuture
    }

  }

  "controller" should {
    "test post by content" in {
      implicit val application = MockApplication()
      implicit val controller = CookieTestController()

      testPost("/test1", Some(StringContent("test"))) {
        response =>
          response.body must_== "content = test"
      }

      testPost("/test2", Some(MapContent(Map("test" -> "test")))) {
        response =>
          response.body must_== "content = test"
      }

    }
  }

}
