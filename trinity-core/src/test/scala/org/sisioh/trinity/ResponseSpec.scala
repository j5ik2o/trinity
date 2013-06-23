package org.sisioh.trinity

import org.jboss.netty.util.CharsetUtil.UTF_8
import org.specs2.mutable.Specification
import org.sisioh.trinity.domain.http.ResponseBuilder
import org.jboss.netty.handler.codec.http.HttpResponseStatus


class ResponseSpec extends Specification {
  def resp = new ResponseBuilder

  ".ok" should {
    "return a 200 response" in {
      resp.withOk.build.status must_== (HttpResponseStatus.valueOf(200))
    }
  }

  ".notFound" should {
    "return a 404 response" in {
      resp.withNotFound.build.status must_== (HttpResponseStatus.valueOf(404))
    }
  }

  ".status(201)" should {
    "return a 201 response" in {
      resp.withStatus(HttpResponseStatus.valueOf(201)).build.status.getCode must_== (201)
    }
  }

  ".plain()" should {
    "return a 200 plain response" in {
      val response = resp.withPlain("howdy")

      response.build.status.getCode must_== (200)
      new String(response.build.body.get.array()) must_== ("howdy")
      response.build.headers("Content-Type") must_== ("text/plain")
    }
  }

  ".nothing()" should {
    "return a 200 empty response" in {
      val response = resp.withNothing

      response.build.status.getCode must_== (200)
      new String(response.build.body.get.array()) must_== ("")
      response.build.headers("Content-Type") must_== ("text/plain")
    }
  }

  ".html()" should {
    "return a 200 html response" in {
      val response = resp.withHtml("<h1>howdy</h1>")

      response.build.status.getCode must_== (200)
      new String(response.build.body.get.array()) must_== ("<h1>howdy</h1>")
      response.build.headers("Content-Type") must_== ("text/html")
    }
  }

  ".json()" should {
    "return a 200 json response" in {
      import org.json4s.JsonDSL._
      import org.json4s.jackson.JsonMethods._
      val response = resp.withJson(Map("foo" -> "bar"))
      val body = response.build.get.getContent.toString(UTF_8)

      response.build.status.getCode must_== (200)
      body must_== ("""{"foo":"bar"}""")
      response.build.headers("Content-Type") must_== ("application/json")
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
