package org.sisioh.trinity.test.cookie

import org.specs2.mutable.Specification
import org.sisioh.trinity.test.ControllerIntegrationTestSupport

/**
 * クッキーのためのスペック(インテグレーション用)。
 */
class CookieIntegrationSpec
  extends Specification with ControllerIntegrationTestSupport {

  "basic k/v cookie" should {
    implicit val application = createApplicationWithRandomPort
    implicit val controller = new CookieTestController()
    "have Foo:Bar" in new WithServer(application, controller) {
      testGetByParams("/sendCookie") {
        response =>
          response.getHeader("Set-Cookie") must_== "Foo=Bar"
      }
    }
  }

  "advanced Cookie" should {
    implicit val application = createApplicationWithRandomPort
    implicit val controller = new CookieTestController()
    "have Biz:Baz&Secure=true" in new WithServer(application, controller) {
      testGetByParams("/sendAdvCookie") {
        response =>
          response.getHeader("Set-Cookie") must_== "Biz=Baz; Secure"
      }
    }
  }
}
