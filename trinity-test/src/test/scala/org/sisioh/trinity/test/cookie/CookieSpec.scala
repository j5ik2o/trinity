package org.sisioh.trinity.test.cookie

import org.sisioh.trinity.application.TrinityApplication
import org.specs2.mutable.Specification
import org.sisioh.trinity.test.{MockApplication, ControllerUnitTestSupport}


class CookieSpec extends Specification with ControllerUnitTestSupport {


  def getController(implicit application: TrinityApplication) =
    new CookieTestController()

  "basic k/v cookie" should {
    implicit val application = MockApplication()
    "have Foo:Bar" in {
      testGetByParams("/sendCookie") {
        response =>
          response.getHeader("Set-Cookie") must_== "Foo=Bar"
      }
    }
  }

  "advanced Cookie" should {
    implicit val application = MockApplication()
    "have Biz:Baz&Secure=true" in {
      testGetByParams("/sendAdvCookie") {
        response =>
          response.getHeader("Set-Cookie") must_== "Biz=Baz; Secure"
      }
    }
  }

}


