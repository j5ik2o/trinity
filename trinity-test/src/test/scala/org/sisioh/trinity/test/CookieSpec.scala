package org.sisioh.trinity.test

import org.jboss.netty.handler.codec.http._
import org.sisioh.trinity.domain.{ScalatraLikeController, RouteRepositoryOnMemory, Config}
import org.sisioh.trinity.application.TrinityApplication
import com.twitter.util.Future

class CookieTestController(application: TrinityApplication) extends ScalatraLikeController(application) {

  get("/sendCookie") {
    request =>
      Future(responseBuilder.withPlain("get:path").withCookie("Foo", "Bar").build)
  }

  get("/sendAdvCookie") {
    val c = new DefaultCookie("Biz", "Baz")
    c.setSecure(true)
    request =>
      Future(responseBuilder.withPlain("get:path").withCookie(c).build)
  }

}

class CookieSpec extends SpecHelper {

  def controller = {
    new CookieTestController(new MockApplication(Config(), new RouteRepositoryOnMemory))
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
