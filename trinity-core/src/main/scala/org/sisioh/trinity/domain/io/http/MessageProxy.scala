package org.sisioh.trinity.domain.io.http

import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

/**
 * Represents the proxy trait for [[Message]].
 */
trait MessageProxy extends Message with Proxy {

  def underlying: Message

  override val isMutable: Boolean = underlying.isMutable

  override def self = underlying

  override def isRequest = underlying.isRequest

  override def getHeader(name: HeaderName): Option[String] = underlying.getHeader(name)

  override def getHeaders(name: HeaderName): Seq[String] = underlying.getHeaders(name)

  override def headers: Seq[(HeaderName, Any)] = underlying.headers

  override def containsHeader(name: HeaderName): Boolean = underlying.containsHeader(name)

  override def headerNames: Set[HeaderName] = underlying.headerNames

  override def protocolVersion: ProtocolVersion.Value = underlying.protocolVersion

  override def withProtocolVersion(version: ProtocolVersion.Value): this.type = mutate {
    _.underlying.withProtocolVersion(version)
  }

  override def content: ChannelBuffer = underlying.content

  override def withContent(content: ChannelBuffer): this.type = mutate {
    _.underlying.withContent(content)
  }

  override def withHeader(name: HeaderName, value: Any): this.type = mutate {
    _.underlying.withHeader(name, value)
  }

  override def withHeader(name: HeaderName, values: Seq[_]): this.type = mutate {
    _.underlying.withHeader(name, values)
  }

  override def withoutHeader(name: HeaderName): this.type = mutate {
    _.underlying.withoutHeader(name)
  }

  override def withoutAllHeaders: this.type = mutate {
    _.underlying.withoutAllHeaders
  }

  override val isChunked: Boolean = underlying.isChunked

  override def withChunked(chunked: Boolean): this.type = mutate {
    _.underlying.withChunked(chunked)
  }

}
