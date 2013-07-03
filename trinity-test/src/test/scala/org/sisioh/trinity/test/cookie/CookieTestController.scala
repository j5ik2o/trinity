package org.sisioh.trinity.test.cookie

import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.controller.SimpleController
import org.jboss.netty.handler.codec.http.DefaultCookie

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
