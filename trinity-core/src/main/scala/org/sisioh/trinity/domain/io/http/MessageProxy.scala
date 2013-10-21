package org.sisioh.trinity.domain.io.http

import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

trait MessageProxy extends Message with Proxy {



  protected def createInstance(message: this.type): this.type

  protected def mutate(f: (this.type) => Unit): this.type = {
    val cloned = if (isMutable) {
      this.asInstanceOf[this.type]
    } else {
      createInstance(this)
    }
    f(cloned)
    cloned
  }

  def underlying: Message

  def self = underlying

  def isRequest = underlying.isRequest

  def getHeader(name: String): Option[String] = underlying.getHeader(name)

  def getHeaders(name: String): Seq[String] = underlying.getHeaders(name)

  def headers: Seq[(String, Any)] = underlying.headers

  def containsHeader(name: String): Boolean = underlying.containsHeader(name)

  def headerNames: Set[String] = underlying.headerNames

  def protocolVersion: Version.Value = underlying.protocolVersion

  def withProtocolVersion(version: Version.Value): this.type = mutate {
    _.underlying.withProtocolVersion(version)
  }

  def content: ChannelBuffer = underlying.content

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
