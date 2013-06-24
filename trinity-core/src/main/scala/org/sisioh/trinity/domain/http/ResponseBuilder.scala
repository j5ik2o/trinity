package org.sisioh.trinity.domain.http

import com.twitter.finagle.http.{Response => FinagleResponse, Request => FinagleRequest}
import org.jboss.netty.buffer.{ChannelBuffers, ChannelBuffer}
import ChannelBuffers._
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.util.CharsetUtil._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import com.twitter.util.{Await, Future}
import scala.collection.JavaConverters._

case class Response
(status: HttpResponseStatus = HttpResponseStatus.OK,
 headers: Map[String, AnyRef] = Map.empty,
 cookies: Seq[Cookie] = Seq.empty,
 body: Option[ChannelBuffer] = None) {

  def this(status: Int,
           headers: Map[String, AnyRef],
           cookies: Seq[Cookie],
           body: Option[ChannelBuffer]) =
    this(HttpResponseStatus.valueOf(status), headers, cookies, body)

  def get: FinagleResponse = {
    val result = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status)
    headers.foreach {
      case (k, v: Iterable[_]) =>
        result.setHeader(k, v.asJava)
      case (k, v) =>
        result.setHeader(k, v)
    }
    if (!cookies.isEmpty) {
      val cookieEncoder = new CookieEncoder(true)
      cookies.foreach {
        xs =>
          cookieEncoder.addCookie(xs)
      }
      result.setHeader("Set-Cookie", cookieEncoder.encode)
    }
    body.foreach {
      b =>
        result.setContent(b)
    }
    FinagleResponse(result)
  }

}


case class ResponseBuilder(responseFuture: Future[Response] = Future(Response())) {

  def withStatus
  (status: Int): ResponseBuilder = {
    withStatus(HttpResponseStatus.valueOf(status))
  }

  def withStatus
  (status: HttpResponseStatus): ResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(status = status)
    }
    ResponseBuilder(newResposeFuture)
  }

  def withCookie
  (tuple: (String, String)): ResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(cookies = response.cookies :+ new DefaultCookie(tuple._1, tuple._2))
    }
    ResponseBuilder(newResposeFuture)
  }

  def withCookie
  (cookie: Cookie): ResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(cookies = response.cookies :+ cookie)
    }
    ResponseBuilder(newResposeFuture)
  }

  def withHeader
  (header: (String, String)): ResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(headers = response.headers + header)
    }
    ResponseBuilder(newResposeFuture)
  }

  def withBody
  (body: Array[Byte]): ResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(body = Some(copiedBuffer(body)))
    }
    ResponseBuilder(newResposeFuture)
  }

  def withBody
  (body: => String): ResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(body = Some(copiedBuffer(body, UTF_8)))
    }
    ResponseBuilder(newResposeFuture)
  }

  def withBodyRenderer
  (bodyRenderer: BodyRenderer): ResponseBuilder = {
    val newResposeFuture = bodyRenderer.render.flatMap {
      body =>
        responseFuture.map {
          response =>
            response.copy(body = Some(copiedBuffer(body, UTF_8)))
        }
    }
    ResponseBuilder(newResposeFuture)
  }

  def withPlain
  (body: => String): ResponseBuilder = {
    withHeader("Content-Type", "text/plain").withBody(body)
  }

  def withHtml(body: => String) = {
    withHeader("Content-Type", "text/html").withBody(body)
  }


  def withJson(jValue: => JValue): ResponseBuilder = {
    withHeader("Content-Type", "application/json").withBody(compact(jValue))
  }

  def withNothing = {
    withHeader("Content-Type", "text/plain").withBody("")
  }

  def withOk = withStatus(HttpResponseStatus.OK)

  def withNotFound = withStatus(HttpResponseStatus.NOT_FOUND)

  def toFuture = responseFuture.map(_.get)

  def getResultByAwait = Await.result(responseFuture)

  def getRawByAwait = Await.result(toFuture)

}

object ResponseBuilder {


}
