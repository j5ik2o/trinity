package org.sisioh.trinity.domain.io.infrastructure.http

import org.jboss.netty.handler.codec.http.{HttpMessage => NettyMessage, CookieEncoder}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.{Cookie, Version, Message}
import scala.collection.JavaConversions._

private[trinity]
abstract class AbstractMessage(val netty: NettyMessage) extends Message {

  protected def createInstance(message: AbstractMessage): this.type

  protected def setHeaders(headers: Seq[(String, Any)]) {
    headers.foreach {
      case (key, value) =>
        value match {
          case values: Iterable[_] => netty.setHeader(key, values)
          case _ => netty.setHeader(key, value)
        }
    }
  }

  protected def setCookies(cookies: Seq[Cookie]) {
    if (cookies.size > 0) {
      val encoder = new CookieEncoder(true)
      cookies.foreach {
        cookie =>
          encoder.addCookie(cookie)
      }
      netty.setHeader("Set-Cookie", encoder.encode())
    }
  }

  protected def setContent(content: ChannelBuffer) {
    netty.setContent(content)
  }

  protected def mutate(f: (NettyMessage) => Unit): this.type = {
    val cloned = createInstance(this)
    f(cloned.netty)
    cloned
  }

  def getHeader(name: String): String = netty.getHeader(name)

  def getHeaders(name: String): Seq[String] = netty.getHeaders(name).toSeq

  def headers: Seq[(String, Any)] = netty.getHeaders.map {
    e => (e.getKey, e.getValue)
  }.toSeq

  def containsHeader(name: String): Boolean = netty.containsHeader(name)

  def headerNames: Set[String] = netty.getHeaderNames.toSet

  def protocolVersion: Version.Value = netty.getProtocolVersion

  def withProtocolVersion(version: Version.Value) = mutate {
    _.setProtocolVersion(version)
  }

  def content: ChannelBuffer = netty.getContent

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

  def isChunked: Boolean = netty.isChunked

  def withChunked(chunked: Boolean) = mutate {
    _.setChunked(chunked)
  }

}
