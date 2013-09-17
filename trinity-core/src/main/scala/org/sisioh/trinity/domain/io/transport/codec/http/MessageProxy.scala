package org.sisioh.trinity.domain.io.transport.codec.http

import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

trait MessageProxy extends Message with Proxy {

  protected def createMessage(message: this.type): this.type

  protected def mutate(f: (this.type) => Unit): this.type = {
    val cloned = createMessage(this)
    f(cloned)
    cloned
  }

  def underlying: Message

  def self = underlying

  def getHeader(name: String): String = underlying.getHeader(name)

  def getHeaders(name: String): Seq[String] = underlying.getHeaders(name)

  val headers: Seq[(String, String)] = underlying.headers

  def containsHeader(name: String): Boolean = underlying.containsHeader(name)

  val headerNames: Set[String] = underlying.headerNames

  val protocolVersion: Version.Value = underlying.protocolVersion

  def withProtocolVersion(version: Version.Value): this.type = mutate {
    _.underlying.withProtocolVersion(version)
  }

  val content: ChannelBuffer = underlying.content

  def withContent(content: ChannelBuffer): this.type = mutate {
    _.underlying.withContent(content)
  }

  def withHeader(name: String, value: Any): this.type = mutate {
    _.underlying.withHeader(name, value)
  }

  def withHeader(name: String, values: Seq[_]): this.type = mutate {
    _.underlying.withHeader(name, values)
  }

  def withoutHeader(name: String): this.type = mutate {
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
