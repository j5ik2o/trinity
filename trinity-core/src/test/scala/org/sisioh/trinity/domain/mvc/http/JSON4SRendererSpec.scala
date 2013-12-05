package org.sisioh.trinity.domain.mvc.http

import org.specs2.mutable.Specification
import org.json4s._
import org.sisioh.trinity.domain.io.http.{Charsets, HeaderNames}

class JSON4SRendererSpec extends Specification {

  "JSON4SRenderer" should {
    "be able to output correct contentType" in {
      val jValue = JObject(
        JField("name", JString("value"))
      )
      val renderer = new JSON4SRenderer(jValue)
      val rb = ResponseBuilder()
      renderer.render(rb)
      val response = rb.build
      response.getHeader(HeaderNames.ContentType) must beSome("application/json")
      response.contentAsString() must_== """{"name":"value"}"""
    }
    "be able to output correct contentType with charset" in {
      val jValue = JObject(
        JField("name", JString("value"))
      )
      val renderer = new JSON4SRenderer(jValue, Some(Charsets.UTF_8))
      val rb = ResponseBuilder()
      renderer.render(rb)
      val response = rb.build
      response.getHeader(HeaderNames.ContentType) must beSome("application/json; charset=utf-8")
      response.contentAsString() must_== """{"name":"value"}"""
    }
  }

}
