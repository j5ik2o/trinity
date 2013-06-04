package org.sisioh.trinity

import org.jboss.netty.util.CharsetUtil.UTF_8
import org.specs2.mutable.Specification

class MockView(val title: String) extends View {
  val template = "mock.mustache"
}

class ResponseSpec extends Specification {
  def resp = new Response

  def view = new MockView("howdy view")

  ".ok" should {
    "return a 200 response" in {
      resp.ok.status must_== (200)
    }
  }

  ".notFound" should {
    "return a 404 response" in {
      resp.notFound.status must_== (404)
    }
  }

  ".status(201)" should {
    "return a 201 response" in {
      resp.status(201).status must_== (201)
    }
  }

  ".plain()" should {
    "return a 200 plain response" in {
      val response = resp.plain("howdy")

      response.status must_== (200)
      response.strBody.get must_== ("howdy")
      response.headers("Content-Type") must_== ("text/plain")
    }
  }

  ".nothing()" should {
    "return a 200 empty response" in {
      val response = resp.nothing

      response.status must_== (200)
      response.strBody.get must_== ("")
      response.headers("Content-Type") must_== ("text/plain")
    }
  }

  ".html()" should {
    "return a 200 html response" in {
      val response = resp.html("<h1>howdy</h1>")

      response.status must_== (200)
      response.strBody.get must_== ("<h1>howdy</h1>")
      response.headers("Content-Type") must_== ("text/html")
    }
  }

  ".json()" should {
    "return a 200 json response" in {
      val response = resp.json(Map("foo" -> "bar"))
      val body = response.build.getContent.toString(UTF_8)

      response.status must_== (200)
      body must_== ("""{"foo":"bar"}""")
      response.headers("Content-Type") must_== ("application/json")
    }
  }

  ".view()" should {
    "return a 200 view response" in {
      val response = resp.view(view)
      val body = response.build.getContent.toString(UTF_8)

      response.status must_==(200)
      body must contain("howdy view")
    }
  }
}
