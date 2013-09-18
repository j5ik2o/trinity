package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import org.jboss.netty.handler.codec.http.{HttpMessage => NettyMessage}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.transport.codec.http.{Version, Message}
import scala.collection.JavaConversions._

private[trinity]
abstract class AbstractMessage(val underlying: NettyMessage) extends Message {

  protected def createMessage(message: AbstractMessage): this.type

  protected def mutate(f: (NettyMessage) => Unit): this.type = {
    val cloned = createMessage(this)
    f(cloned.underlying)
    cloned
  }

  def getHeader(name: String): String = underlying.getHeader(name)

  def getHeaders(name: String): Seq[String] = underlying.getHeaders(name).toSeq

  def headers: Seq[(String, Any)] = underlying.getHeaders.map {
    e => (e.getKey, e.getValue)
  }.toSeq

  def containsHeader(name: String): Boolean = underlying.containsHeader(name)

  def headerNames: Set[String] = underlying.getHeaderNames.toSet

  def protocolVersion: Version.Value = underlying.getProtocolVersion

  def withProtocolVersion(version: Version.Value) = mutate {
    _.setProtocolVersion(version)
  }

  def content: ChannelBuffer = underlying.getContent

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

  def isChunked: Boolean = underlying.isChunked

  def withChunked(chunked: Boolean) = mutate {
    _.setChunked(chunked)
  }

}
