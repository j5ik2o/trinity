package org.sisioh.trinity.test

import org.specs2.mutable.Specification
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.controller.{Controller, SimpleController}

class ControllerUnitTestSupportSpec extends Specification with ControllerUnitTestSupport {

  class CookieTestController()(implicit application: TrinityApplication)
    extends SimpleController {

    post("/") {
      request =>
        responseBuilder.withPlain("content = " + request.contentString).toFinagleResponseFuture
    }

  }

  def getController(implicit application: TrinityApplication): Controller =
    new CookieTestController()

  "" should {
    "" in {
      implicit val application = MockApplication()
      testPostByContent("/", Some("test")) {
        response =>
          println("body = (" + response.body + ")")
          response.body must_== "content = test"
      }

    }
  }

}
