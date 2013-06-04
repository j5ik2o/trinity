package org.sisioh.trinity

import org.jboss.netty.handler.codec.http._

class CookieApp extends Controller {

  get("/sendCookie") {
    request => render.withPlain("get:path").withCookie("Foo", "Bar").toFuture
  }

  get("/sendAdvCookie") {
    val c = new DefaultCookie("Biz", "Baz")
    c.setSecure(true)
    request => render.withPlain("get:path").withCookie(c).toFuture
  }

}

class CookieSpec extends SpecHelper {

  def app = {
    new CookieApp
  }

  "basic k/v cookie" should {
    "have Foo:Bar" in {
      get("/sendCookie")
      response.getHeader("Set-Cookie") must_== "Foo=Bar"
    }
  }

  "advanced Cookie" should {
    "have Biz:Baz&Secure=true" in {
      get("/sendAdvCookie")
      response.getHeader("Set-Cookie") must_== "Biz=Baz; Secure"
    }
  }

}
