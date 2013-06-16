package org.sisioh.trinity

import com.twitter.finagle.http.{Response => FinagleResponse, Request => FinagleRequest}
import com.twitter.util.Future
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.util.CharsetUtil.UTF_8
import org.json4s.jackson.JsonMethods._
import org.sisioh.scala.toolbox.LoggingEx
import org.json4s.JsonAST.JValue
import org.sisioh.trinity.domain.BodyRender


object ResponseAdapter {

  def apply(resp: Future[ResponseBuilder]): Future[FinagleResponse] = {
    resp.map(_.build)
  }

}

object ResponseBuilder {

  def apply(body: String): ResponseBuilder = ResponseBuilder().withBody(body).withStatus(200)

  def apply(status: Int, body: String): ResponseBuilder = ResponseBuilder().withBody(body).withStatus(status)

  def apply(status: Int, body: String, headers: Map[String, String]): ResponseBuilder = ResponseBuilder().withBody(body).withStatus(status).withHeaders(headers)

}

case class ResponseBuilder
(status: Int = 200,
 headers: Map[String, String] = Map.empty,
 cookies: Seq[Cookie] = Seq.empty,
 body: Option[ChannelBuffer] = None) extends LoggingEx {


  def withCookie(tuple: (String, String)): ResponseBuilder = {
    copy(cookies = cookies :+ new DefaultCookie(tuple._1, tuple._2))
  }

  def withCookie(cookie: Cookie): ResponseBuilder = {
    copy(cookies = cookies :+ cookie)
  }

  def withHeader(header: (String, String)): ResponseBuilder =
    copy(headers = headers + header)

  def withHeaders(_headers: Map[String, String]): ResponseBuilder =
    copy(headers = headers ++ _headers)

  def withStatus(value: Int): ResponseBuilder =
    copy(status = value)

  def withBody(body: Array[Byte]): ResponseBuilder =
    copy(body = Some(copiedBuffer(body)))

  def withBody(body: String): ResponseBuilder =
    copy(body = Some(copiedBuffer(body, UTF_8)))

  def withBody(bodyRender: BodyRender): ResponseBuilder =
    withBody(bodyRender.render)

  def withPlain(body: String): ResponseBuilder = {
    withHeader("Content-Type", "text/plain").withBody(body)
  }

  def withHtml(body: String) = {
    withHeader("Content-Type", "text/html").withBody(body)
  }

  def withJson(jValue: JValue): ResponseBuilder = {
    withHeader("Content-Type", "application/json").withBody(compact(jValue))
  }

  def withNothing = {
    withHeader("Content-Type", "text/plain").withBody("")
  }

  def withOk = withStatus(200)

  def withNotFound = withStatus(404)

  def build: FinagleResponse = {
    val responseStatus = HttpResponseStatus.valueOf(status)
    val resp = new DefaultHttpResponse(HTTP_1_1, responseStatus)
    headers.foreach {
      xs =>
        resp.setHeader(xs._1, xs._2)
    }
    if (!cookies.isEmpty) {
      val cookieEncoder = new CookieEncoder(true)
      cookies.foreach {
        xs =>
          cookieEncoder.addCookie(xs)
      }
      resp.setHeader("Set-Cookie", cookieEncoder.encode)
    }
    body.foreach {
      b =>
        resp.setContent(b)
    }
    FinagleResponse(resp)
  }

  def toFuture = {
    Future.value(this)
  }

  override def toString = {
    val buf = new StringBuilder
    buf.append(getClass.getSimpleName)
    buf.append('\n')
    buf.append(HTTP_1_1.toString)
    buf.append(' ')
    buf.append(this.status)
    buf.append('\n')
    appendCollection[String, String](buf, this.headers)
    buf.toString()
  }

  private def appendCollection[A, B](buf: StringBuilder, x: Map[A, B]) {
    x foreach {
      xs =>
        buf.append(xs._1)
        buf.append(" : ")
        buf.append(xs._2)
    }
  }
}
