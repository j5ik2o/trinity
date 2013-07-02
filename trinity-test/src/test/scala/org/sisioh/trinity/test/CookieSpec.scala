package org.sisioh.trinity.test

import org.jboss.netty.handler.codec.http._
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.controller.SimpleController
import org.specs2.mutable.Specification

class CookieTestController()(implicit application: TrinityApplication)
  extends SimpleController {

  get("/sendCookie") {
    request =>
      responseBuilder.withPlain("get:path").withCookie("Foo", "Bar").toFuture
  }

  get("/sendAdvCookie") {
    val c = new DefaultCookie("Biz", "Baz")
    c.setSecure(true)
    request =>
      responseBuilder.withPlain("get:path").withCookie(c).toFuture
  }

}

class CookieSpec extends Specification with ControllerTestSupport {

  implicit val application = MockApplication()

  def getController = {
    new CookieTestController()
  }

  "basic k/v cookie" should {
    "have Foo:Bar" in {
      testGet("/sendCookie") {
        response =>
          response.getHeader("Set-Cookie") must_== "Foo=Bar"
      }
    }
  }

  "advanced Cookie" should {
    "have Biz:Baz&Secure=true" in {
      testGet("/sendAdvCookie") {
        response =>
          response.getHeader("Set-Cookie") must_== "Biz=Baz; Secure"
      }
    }
  }

}
