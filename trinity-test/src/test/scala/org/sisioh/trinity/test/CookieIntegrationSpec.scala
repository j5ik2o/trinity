package org.sisioh.trinity.test

import org.specs2.mutable.Specification
import org.sisioh.trinity.application.TrinityApplication

class CookieIntegrationSpec
  extends Specification with ControllerIntegrationTestSupport {

  def getController(implicit application: TrinityApplication) =
    new CookieTestController()

  "basic k/v cookie" should {
    implicit val application = createApplicationWithRandomPort
    "have Foo:Bar" in new WithServer(application) {
      testGet("/sendCookie") {
        response =>
          response.getHeader("Set-Cookie") must_== "Foo=Bar"
      }
    }
  }

  "advanced Cookie" should {
    implicit val application = createApplicationWithRandomPort
    "have Biz:Baz&Secure=true" in new WithServer(application) {
      testGet("/sendAdvCookie") {
        response =>
          response.getHeader("Set-Cookie") must_== "Biz=Baz; Secure"
      }
    }
  }

}
