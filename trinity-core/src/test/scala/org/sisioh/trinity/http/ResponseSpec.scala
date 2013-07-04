package org.sisioh.trinity.http

import org.jboss.netty.util.CharsetUtil.UTF_8
import org.specs2.mutable.Specification
import org.sisioh.trinity.domain.http.TrinityResponseBuilder
import org.jboss.netty.handler.codec.http.HttpResponseStatus


class ResponseSpec extends Specification {

  sequential

  def responseBuilder = new TrinityResponseBuilder

  ".ok" should {
    "return a 200 response" in {
      responseBuilder.withOk.getTrinityResponse.status must_== (HttpResponseStatus.valueOf(200))
    }
  }

  ".notFound" should {
    "return a 404 response" in {
      responseBuilder.withNotFound.getTrinityResponse.status must_== (HttpResponseStatus.valueOf(404))
    }
  }

  ".status(201)" should {
    "return a 201 response" in {
      responseBuilder.withStatus(HttpResponseStatus.valueOf(201)).getTrinityResponse.status.getCode must_== (201)
    }
  }

  ".plain()" should {
    "return a 200 plain response" in {
      val rb = responseBuilder.withPlain("howdy")
      rb.getTrinityResponse.status.getCode must_== 200
      rb.getTrinityResponse.body.get.toString(UTF_8) must_== "howdy"
      rb.getTrinityResponse.headers("Content-Type") must_== ("text/plain")
    }
  }

  ".nothing()" should {
    "return a 200 empty response" in {
      val rb = responseBuilder.withNothing
      rb.getTrinityResponse.status.getCode must_== 200
      rb.getTrinityResponse.body.get.toString(UTF_8) must_== ""
      rb.getTrinityResponse.headers("Content-Type") must_== "text/plain"
    }
  }

  ".html()" should {
    "return a 200 html response" in {
      val rb = responseBuilder.withHtml("<h1>howdy</h1>")
      rb.getTrinityResponse.status.getCode must_== 200
      rb.getTrinityResponse.body.get.toString(UTF_8) must_== "<h1>howdy</h1>"
      rb.getTrinityResponse.headers("Content-Type") must_== "text/html"
    }
  }

  ".json()" should {
    "return a 200 json response" in {
      import org.json4s.JsonDSL._
      val rb = responseBuilder.withJson(Map("foo" -> "bar"))
      rb.getTrinityResponse.status.getCode must_== (200)
      rb.getTrinityResponse.bodyAsString.get must_== ("""{"foo":"bar"}""")
      rb.getTrinityResponse.headers("Content-Type") must_== ("application/json")
    }
  }

  //  ".view()" should {
  //    "return a 200 view response" in {
  //      val response = resp.withView(view)
  //      val body = response.build.getContent.toString(UTF_8)
  //
  //      response.status must_==(200)
  //      body must contain("howdy view")
  //    }
  //  }
}
