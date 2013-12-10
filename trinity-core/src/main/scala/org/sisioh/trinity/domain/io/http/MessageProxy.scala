package org.sisioh.trinity.domain.io.http

import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

trait MessageProxy extends Message with Proxy {

  def underlying: Message

  val isMutable: Boolean = underlying.isMutable

  def self = underlying

  def isRequest = underlying.isRequest

  def getHeader(name: HeaderName): Option[String] = underlying.getHeader(name)

  def getHeaders(name: HeaderName): Seq[String] = underlying.getHeaders(name)

  def headers: Seq[(HeaderName, Any)] = underlying.headers

  def containsHeader(name: HeaderName): Boolean = underlying.containsHeader(name)

  def headerNames: Set[HeaderName] = underlying.headerNames

  def protocolVersion: ProtocolVersion.Value = underlying.protocolVersion

  def withProtocolVersion(version: ProtocolVersion.Value): this.type = mutate {
    _.underlying.withProtocolVersion(version)
  }

  def content: ChannelBuffer = underlying.content

  def withContent(content: ChannelBuffer): this.type = mutate {
    _.underlying.withContent(content)
  }

  def withHeader(name: HeaderName, value: Any): this.type = mutate {
    _.underlying.withHeader(name, value)
  }

  def withHeader(name: HeaderName, values: Seq[_]): this.type = mutate {
    _.underlying.withHeader(name, values)
  }

  def withoutHeader(name: HeaderName): this.type = mutate {
    _.underlying.withoutHeader(name)
  }

  def withoutAllHeaders: this.type = mutate {
    _.underlying.withoutAllHeaders
  }

  val isChunked: Boolean = underlying.isChunked

  def withChunked(chunked: Boolean): this.type = mutate {
    _.underlying.withChunked(chunked)
  }

}
