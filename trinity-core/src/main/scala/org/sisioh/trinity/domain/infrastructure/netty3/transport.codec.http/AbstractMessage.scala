package org.sisioh.trinity.domain.infrastructure.netty3.transport.codec.http

import org.jboss.netty.handler.codec.http.HttpVersion
import org.jboss.netty.handler.codec.http.{HttpMessage => NettyMessage}
import org.sisioh.trinity.domain.buffer.ChannelBuffer
import org.sisioh.trinity.domain.infrastructure.netty3.buffer.ChannelBuffersImpl._
import scala.collection.JavaConversions._
import org.sisioh.trinity.domain.transport.codec.http.Message

private[trinity]
abstract class AbstractMessage(val underlying: NettyMessage) extends Message {

  protected def createMessage(message: AbstractMessage): AbstractMessage

  protected def mutate(f: (NettyMessage) => Unit): AbstractMessage = {
    val cloned = createMessage(this)
    f(cloned.underlying)
    cloned
  }

  def getHeader(name: String): String = underlying.getHeader(name)

  def getHeaders(name: String): Seq[String] = underlying.getHeaders(name).toSeq

  def getHeaders: Seq[(String, String)] = underlying.getHeaders.map {
    e => (e.getKey, e.getValue)
  }.toSeq

  def containsHeader(name: String): Boolean = underlying.containsHeader(name)

  def getHeaderNames: Set[String] = underlying.getHeaderNames.toSet

  def getProtocolVersion: HttpVersion = underlying.getProtocolVersion

  def withProtocolVersion(version: HttpVersion): Message = mutate {
    _.setProtocolVersion(version)
  }

  def getContent: ChannelBuffer = ChannelBuffer.from(underlying.getContent)

  def withContent(content: ChannelBuffer): Message = mutate {
    _.setContent(content)
  }

  def withHeader(name: String, value: Any): Message = mutate {
    _.addHeader(name, value)
  }

  def withHeader(name: String, values: Seq[_]): Message = mutate {
    _.setHeader(name, values)
  }

  def withoutHeader(name: String): Message = mutate {
    _.removeHeader(name)
  }

  def withoutAllHeaders: Message = mutate {
    _.clearHeaders()
  }

  def isChunked: Boolean = underlying.isChunked

  def withChunked(chunked: Boolean): Message = mutate {
    _.setChunked(chunked)
  }

}
