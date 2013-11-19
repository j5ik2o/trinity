package org.sisioh.trinity.domain.io.infrastructure.http

import com.twitter.finagle.http.{Message => FinagleMessage}
import org.jboss.netty.handler.codec.http.{HttpMessage => NettyMessage, CookieEncoder}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http._
import scala.collection.JavaConversions._

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

  def getHeader(name: HeaderName): Option[String] = Option(toUnderlyingAsFinagle.getHeader(name.asString))

  def getHeaders(name: HeaderName): Seq[String] = toUnderlyingAsFinagle.getHeaders(name.asString).toList

  def headers: Seq[(HeaderName, Any)] = toUnderlyingAsFinagle.getHeaders.toList.map {
    e => (HeaderNames.valueOf(e.getKey), e.getValue)
  }

  def containsHeader(name: HeaderName): Boolean = toUnderlyingAsFinagle.containsHeader(name.asString)

  def headerNames: Set[HeaderName] = toUnderlyingAsFinagle.getHeaderNames.map(HeaderNames.valueOf).toSet

  def protocolVersion: ProtocolVersion.Value = toUnderlyingAsFinagle.getProtocolVersion

  def withProtocolVersion(version: ProtocolVersion.Value) = mutate {
    _.setProtocolVersion(version)
  }

  def content: ChannelBuffer = toUnderlyingAsFinagle.getContent

  def withContent(content: ChannelBuffer) = mutate {
    _.setContent(content)
  }

  def withHeader(name: HeaderName, value: Any) = mutate {
    _.addHeader(name.asString, value)
  }

  def withHeader(name: HeaderName, values: Seq[_]) = mutate {
    _.setHeader(name.asString, values)
  }

  def withoutHeader(name: HeaderName) = mutate {
    _.removeHeader(name.asString)
  }

  def withoutAllHeaders = mutate {
    _.clearHeaders()
  }

  def isChunked: Boolean = toUnderlyingAsFinagle.isChunked

  def withChunked(chunked: Boolean) = mutate {
    _.setChunked(chunked)
  }

}
