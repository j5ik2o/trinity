package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Message => FinagleMessage}
import org.jboss.netty.handler.codec.http.{HttpMessage => NettyMessage, CookieEncoder}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import scala.collection.JavaConversions._

/**
 * Represents a abstract class for [[Message]].
 *
 * @param toUnderlyingAsFinagle [[FinagleMessage]]
 */
private[trinity]
abstract class AbstractMessage(val toUnderlyingAsFinagle: FinagleMessage) extends Message {

  override def withAttributes(_attributes: Map[String, Any]): this.type = {
    createInstance(this, this.attributes ++ _attributes)
  }

  override def withAttributes(_attributes: (String, Any)*): this.type = {
    createInstance(this, this.attributes ++ _attributes)
  }

  override def withoutAllAttributes(): this.type =
    createInstance(this, Map.empty)

  /**
   * Sets headers as key-value set.
   *
   * @param headers headers as key-value set
   */
  protected def setHeaders(headers: Seq[(HeaderName, Any)]) {
    headers.foreach {
      case (key, value) =>
        value match {
          case values: Iterable[_] => toUnderlyingAsFinagle.setHeader(key.asString, values)
          case _ => toUnderlyingAsFinagle.setHeader(key.asString, value)
        }
    }
  }

  private val cookieHeaderName = if (isResponse) "Set-Cookie" else "Cookie"

  /**
   * Sets cookie values.
   *
   * @param cookies cookie values
   */
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

  /**
   * Sets a content as [[ChannelBuffer]]
   *
   * @param content
   */
  protected def setContent(content: ChannelBuffer) {
    toUnderlyingAsFinagle.setContent(content)
  }

  /**
   * Mutates instance.
   *
   * @param f function
   * @return instance
   */
  protected def mutateAsNettyMessage(f: (NettyMessage) => Unit): this.type = {
    val cloned = if (isMutable) {
      this.asInstanceOf[this.type]
    } else {
      createInstance(this, attributes)
    }
    f(cloned.toUnderlyingAsFinagle)
    cloned
  }

  override def getHeader(name: HeaderName): Option[String] =
    Option(toUnderlyingAsFinagle.getHeader(name.asString))

  override def getHeaders(name: HeaderName): Seq[String] =
    toUnderlyingAsFinagle.getHeaders(name.asString).toList

  override def headers: Seq[(HeaderName, Any)] = toUnderlyingAsFinagle.getHeaders.toList.map {
    e => (HeaderNames.valueOf(e.getKey), e.getValue)
  }

  override def containsHeader(name: HeaderName): Boolean = toUnderlyingAsFinagle.containsHeader(name.asString)

  override def headerNames: Set[HeaderName] = toUnderlyingAsFinagle.getHeaderNames.map(HeaderNames.valueOf).toSet

  override def protocolVersion: ProtocolVersion.Value = toUnderlyingAsFinagle.getProtocolVersion

  override def withProtocolVersion(version: ProtocolVersion.Value) = mutateAsNettyMessage {
    _.setProtocolVersion(version)
  }

  override def content: ChannelBuffer = toUnderlyingAsFinagle.getContent

  override def withContent(content: ChannelBuffer) = mutate {
    _.setContent(content)
  }

  override def withHeader(name: HeaderName, value: Any) = mutateAsNettyMessage {
    _.addHeader(name.asString, value)
  }

  override def withHeader(name: HeaderName, values: Seq[_]) = mutateAsNettyMessage {
    _.setHeader(name.asString, values)
  }

  override def withoutHeader(name: HeaderName) = mutateAsNettyMessage {
    _.removeHeader(name.asString)
  }

  override def withoutAllHeaders = mutateAsNettyMessage {
    _.clearHeaders()
  }

  override def isChunked: Boolean = toUnderlyingAsFinagle.isChunked

  override def withChunked(chunked: Boolean) = mutateAsNettyMessage {
    _.setChunked(chunked)
  }

}
