package org.sisioh.trinity.domain.io.infrastructure.http

import org.jboss.netty.handler.codec.http.{HttpMessage => NettyMessage, CookieEncoder}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.{Cookie, ProtocolVersion, Message}
import scala.collection.JavaConversions._
import com.twitter.finagle.http.{Message => FinagleMessage}

private[trinity]
abstract class AbstractMessage(val toUnderlyingAsFinagle: FinagleMessage) extends Message {

  protected def createInstance(message: AbstractMessage): this.type

  protected def setHeaders(headers: Seq[(String, Any)]) {
    headers.foreach {
      case (key, value) =>
        value match {
          case values: Iterable[_] => toUnderlyingAsFinagle.setHeader(key, values)
          case _ => toUnderlyingAsFinagle.setHeader(key, value)
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
      toUnderlyingAsFinagle.setHeader(cookieHeaderName, encoder.encode())
    }
  }

  protected def setContent(content: ChannelBuffer) {
    toUnderlyingAsFinagle.setContent(content)
  }

  protected def mutate(f: (NettyMessage) => Unit): this.type = {
    val cloned = createInstance(this)
    f(cloned.toUnderlyingAsFinagle)
    cloned
  }

  def getHeader(name: String): Option[String] = Option(toUnderlyingAsFinagle.getHeader(name))

  def getHeaders(name: String): Seq[String] = toUnderlyingAsFinagle.getHeaders(name).toList

  def headers: Seq[(String, Any)] = toUnderlyingAsFinagle.getHeaders.toList.map {
    e => (e.getKey, e.getValue)
  }

  def containsHeader(name: String): Boolean = toUnderlyingAsFinagle.containsHeader(name)

  def headerNames: Set[String] = toUnderlyingAsFinagle.getHeaderNames.toSet

  def protocolVersion: ProtocolVersion.Value = toUnderlyingAsFinagle.getProtocolVersion

  def withProtocolVersion(version: ProtocolVersion.Value) = mutate {
    _.setProtocolVersion(version)
  }

  def content: ChannelBuffer = toUnderlyingAsFinagle.getContent

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

  def isChunked: Boolean = toUnderlyingAsFinagle.isChunked

  def withChunked(chunked: Boolean) = mutate {
    _.setChunked(chunked)
  }

}
