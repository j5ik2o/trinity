package org.sisioh.trinity.test

import org.specs2.mutable.Specification
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.controller.SimpleController

class ControllerUnitTestSupportSpec
  extends Specification
  with ControllerUnitTestSupport {

  case class CookieTestController()(implicit application: TrinityApplication)
    extends SimpleController {

    post("/") {
      request =>
        responseBuilder.withPlain("content = " + request.contentString).toFinagleResponseFuture
    }

  }

  "controller" should {
    "test post by content" in {
      implicit val application = MockApplication()
      implicit val controller = CookieTestController()

      testPostByContent("/", Some("test")) {
        response =>
          response.body must_== "content = test"
      }

    }
  }

}
