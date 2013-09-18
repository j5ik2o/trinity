package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import org.specs2.mutable.Specification
import org.sisioh.trinity.domain.io.transport.codec.http.{Method, Version}

class RequestImplSpec extends Specification {

  "request" should {
    "not have headerNames" in {
      val target = new RequestImpl(Version.Http11, Method.Get, "/index")
      target.headerNames must haveSize(0)
    }
    "have headerNames" in {
      val target = new RequestImpl(Version.Http11, Method.Get, "/index")
      target.withHeader("hoge", "").headerNames must haveSize(1)
    }
    "have a chunked value which false" in {
      val target = new RequestImpl(Version.Http11, Method.Get, "/index")
      target.isChunked must beFalse
    }
    "have a chunked value which true" in {
      val target = new RequestImpl(Version.Http11, Method.Get, "/index")
      target.withChunked(chunked = true).isChunked must beTrue
    }
  }

}
