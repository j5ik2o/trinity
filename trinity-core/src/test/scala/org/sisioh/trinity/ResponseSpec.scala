package org.sisioh.trinity

import org.jboss.netty.util.CharsetUtil.UTF_8
import org.specs2.mutable.Specification
import org.sisioh.trinity.domain.http.ResponseBuilder
import org.jboss.netty.handler.codec.http.HttpResponseStatus


class ResponseSpec extends Specification {

  sequential

  def resp = new ResponseBuilder

  ".ok" should {
    "return a 200 response" in {
      resp.withOk.getFinagleResponse.status must_== (HttpResponseStatus.valueOf(200))
    }
  }

  ".notFound" should {
    "return a 404 response" in {
      resp.withNotFound.getFinagleResponse.status must_== (HttpResponseStatus.valueOf(404))
    }
  }

  ".status(201)" should {
    "return a 201 response" in {
      resp.withStatus(HttpResponseStatus.valueOf(201)).getFinagleResponse.status.getCode must_== (201)
    }
  }

  ".plain()" should {
    "return a 200 plain response" in {
      val response = resp.withPlain("howdy")

      response.getFinagleResponse.status.getCode must_== 200
      response.getResponse.body.get.toString(UTF_8) must_== "howdy"
      response.getFinagleResponse.headers("Content-Type") must_== ("text/plain")
    }
  }

  ".nothing()" should {
    "return a 200 empty response" in {
      val response = resp.withNothing
      response.getFinagleResponse.status.getCode must_== 200
      response.getResponse.body.get.toString(UTF_8) must_== ""
      response.getFinagleResponse.headers("Content-Type") must_== "text/plain"
    }
  }

  ".html()" should {
    "return a 200 html response" in {
      val response = resp.withHtml("<h1>howdy</h1>")
      response.getFinagleResponse.status.getCode must_== 200
      response.getResponse.body.get.toString(UTF_8) must_== "<h1>howdy</h1>"
      response.getFinagleResponse.headers("Content-Type") must_== "text/html"
    }
  }

  ".json()" should {
    "return a 200 json response" in {
      import org.json4s.JsonDSL._
      val response = resp.withJson(Map("foo" -> "bar"))
      val body = response.getFinagleResponse.getContent.toString(UTF_8)
      response.getFinagleResponse.status.getCode must_== (200)
      body must_== ("""{"foo":"bar"}""")
      response.getFinagleResponse.headers("Content-Type") must_== ("application/json")
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
