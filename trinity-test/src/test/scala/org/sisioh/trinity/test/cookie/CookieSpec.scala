package org.sisioh.trinity.test.cookie

import org.specs2.mutable.Specification
import org.sisioh.trinity.test.{MockApplication, ControllerUnitTestSupport}
import scala.util.Success

class CookieSpec extends Specification with ControllerUnitTestSupport {

  "basic k/v cookie" should {
    implicit val application = MockApplication()
    implicit val controller = new CookieTestController()
    "have Foo:Bar" in {
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
    implicit val application = MockApplication()
    implicit val controller = new CookieTestController()
    "have Biz:Baz&Secure=true" in {
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


