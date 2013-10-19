package org.sisioh.trinity.domain.io.infrastructure.http

import org.jboss.netty.handler.codec.http.{HttpMessage => NettyMessage, CookieEncoder}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.{Cookie, Version, Message}
import scala.collection.JavaConversions._
import com.twitter.finagle.http.{Message => FinagleMessage}

private[trinity]
abstract class AbstractMessage(val finagle: FinagleMessage) extends Message {

  protected def createInstance(message: AbstractMessage): this.type

  protected def setHeaders(headers: Seq[(String, Any)]) {
    headers.foreach {
      case (key, value) =>
        value match {
          case values: Iterable[_] => finagle.setHeader(key, values)
          case _ => finagle.setHeader(key, value)
        }
    }
  }

  private val cookieHeaderName = if (isResponse) "Set-Cookie" else "Cookie"

  protected def setCookies(cookies: Seq[Cookie]) {
    if (cookies.size > 0) {
      val encoder = new CookieEncoder(true)
      cookies.foreach {
        cookie =>
          encoder.addCookie(cookie)
      }
      finagle.setHeader(cookieHeaderName, encoder.encode())
    }
  }

  protected def setContent(content: ChannelBuffer) {
    finagle.setContent(content)
  }

  protected def mutate(f: (NettyMessage) => Unit): this.type = {
    val cloned = createInstance(this)
    f(cloned.finagle)
    cloned
  }

  def getHeader(name: String): Option[String] = Option(finagle.getHeader(name))

  def getHeaders(name: String): Seq[String] = finagle.getHeaders(name).toSeq

  def headers: Seq[(String, Any)] = finagle.getHeaders.map {
    e => (e.getKey, e.getValue)
  }.toSeq

  def containsHeader(name: String): Boolean = finagle.containsHeader(name)

  def headerNames: Set[String] = finagle.getHeaderNames.toSet

  def protocolVersion: Version.Value = finagle.getProtocolVersion

  def withProtocolVersion(version: Version.Value) = mutate {
    _.setProtocolVersion(version)
  }

  def content: ChannelBuffer = finagle.getContent

  def withContent(content: ChannelBuffer) = mutate {
    _.setContent(content)
  }

  def withHeader(name: String, value: Any) = mutate {
    _.addHeader(name, value)
  }

  def withHeader(name: String, values: Seq[_]) = mutate {
    _.setHeader(name, values)
  }

  def withoutHeader(name: String) = mutate {
    _.removeHeader(name)
  }

  def withoutAllHeaders = mutate {
    _.clearHeaders()
  }

  def isChunked: Boolean = finagle.isChunked

  def withChunked(chunked: Boolean) = mutate {
    _.setChunked(chunked)
  }

}
