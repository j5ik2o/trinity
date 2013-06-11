package org.sisioh.trinity

import org.jboss.netty.util.CharsetUtil.UTF_8
import org.specs2.mutable.Specification


class ResponseSpec extends Specification {
  def resp = new ResponseBuilder

  ".ok" should {
    "return a 200 response" in {
      resp.withOk.status must_== (200)
    }
  }

  ".notFound" should {
    "return a 404 response" in {
      resp.withNotFound.status must_== (404)
    }
  }

  ".status(201)" should {
    "return a 201 response" in {
      resp.withStatus(201).status must_== (201)
    }
  }

  ".plain()" should {
    "return a 200 plain response" in {
      val response = resp.withPlain("howdy")

      response.status must_== (200)
      new String(response.body.get.array()) must_== ("howdy")
      response.headers("Content-Type") must_== ("text/plain")
    }
  }

  ".nothing()" should {
    "return a 200 empty response" in {
      val response = resp.withNothing

      response.status must_== (200)
      new String(response.body.get.array()) must_== ("")
      response.headers("Content-Type") must_== ("text/plain")
    }
  }

  ".html()" should {
    "return a 200 html response" in {
      val response = resp.withHtml("<h1>howdy</h1>")

      response.status must_== (200)
      new String(response.body.get.array()) must_== ("<h1>howdy</h1>")
      response.headers("Content-Type") must_== ("text/html")
    }
  }

  ".json()" should {
    "return a 200 json response" in {
      val response = resp.withJson(Map("foo" -> "bar"))
      val body = response.build.getContent.toString(UTF_8)

      response.status must_== (200)
      body must_== ("""{"foo":"bar"}""")
      response.headers("Content-Type") must_== ("application/json")
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
