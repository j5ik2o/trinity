package org.sisioh.trinity.domain.io.infrastructure.http

import org.junit.runner.RunWith
import org.sisioh.trinity.domain.io.buffer.ChannelBuffers
import org.sisioh.trinity.domain.io.http._
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RequestImplSpec extends Specification {

  "request" should {
    "get version" in {
      val target = new RequestImpl(Methods.Get, "/index", protocolVersion = ProtocolVersion.Http11)
      target.protocolVersion must_== ProtocolVersion.Http11
    }
    "get uri" in {
      val target = new RequestImpl(Methods.Get, "/index", protocolVersion = ProtocolVersion.Http11)
      target.uri must_== "/index"
    }
    "get method" in {
      val target = new RequestImpl(Methods.Get, "/index", protocolVersion = ProtocolVersion.Http11)
      target.method must_== Methods.Get
    }
    "contains header" in {
      val target = new RequestImpl(Methods.Get, "/index", protocolVersion = ProtocolVersion.Http11)
      target.withHeader(HeaderNames.ofAny("name"), "value").containsHeader(HeaderNames.ofAny("name")) must beTrue
    }
    "contains header" in {
      val target = new RequestImpl(Methods.Get, "/index", protocolVersion = ProtocolVersion.Http11)
      target.withContent(ChannelBuffers.copiedBuffer("test", Charsets.UTF_8)).content.toString(Charsets.UTF_8) must_== "test"
    }
    "not have headerNames" in {
      val target = new RequestImpl(Methods.Get, "/index", protocolVersion = ProtocolVersion.Http11)
      target.headerNames must haveSize(0)
    }
    "have headerNames" in {
      val target = new RequestImpl(Methods.Get, "/index", protocolVersion = ProtocolVersion.Http11)
      target.withHeader(HeaderNames.ofAny("name"), "value").headerNames must haveSize(1)
    }
    "have a chunked value which false" in {
      val target = new RequestImpl(Methods.Get, "/index", protocolVersion = ProtocolVersion.Http11)
      target.isChunked must beFalse
    }
    "have a chunked value which true" in {
      val target = new RequestImpl(Methods.Get, "/index", protocolVersion = ProtocolVersion.Http11)
      target.withChunked(chunked = true).isChunked must beTrue
    }

  }

}
