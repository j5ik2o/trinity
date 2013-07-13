package org.sisioh.trinity.test.cookie

import org.specs2.mutable.Specification
import org.sisioh.trinity.test.ControllerIntegrationTestSupport
import scala.util.Success

/**
 * クッキーのためのスペック(インテグレーション用)。
 */
class CookieIntegrationSpec
  extends Specification with ControllerIntegrationTestSupport {

  "basic k/v cookie" should {
    implicit val application = createApplicationWithRandomPort
    implicit val controller = new CookieTestController()
    "have Foo:Bar" in new WithServer {
      testGet("/sendCookie") {
        responseTry =>
        responseTry must beSuccessfulTry.like {
          case response =>
            response.getHeader("Set-Cookie") must_== "Foo=Bar"
        }
      }
    }
  }

  "advanced Cookie" should {
    implicit val application = createApplicationWithRandomPort
    implicit val controller = new CookieTestController()
    "have Biz:Baz&Secure=true" in new WithServer {
      testGet("/sendAdvCookie") {
        responseTry =>
        responseTry must beSuccessfulTry.like {
          case response =>
            response.getHeader("Set-Cookie") must_== "Biz=Baz; Secure"
        }
      }
    }
  }
}
