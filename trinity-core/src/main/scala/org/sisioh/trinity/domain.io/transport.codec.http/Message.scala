package org.sisioh.trinity.domain.io.transport.codec.http

import org.jboss.netty.handler.codec.http.HttpVersion
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

/**
 * HTTPのメッセージを表すトレイト。
 */
trait Message {

  def getHeader(name: String): String

  def getHeaders(name: String): Seq[String]

  val headers: Seq[(String, String)]

  def containsHeader(name: String): Boolean

  val headerNames: Set[String]

  val protocolVersion: HttpVersion

  def withProtocolVersion(version: HttpVersion): Message

  val content: ChannelBuffer

  def withContent(content: ChannelBuffer): Message

  def withHeader(name: String, value: Any): Message

  def withHeader(name: String, values: Seq[_]): Message

  def withoutHeader(name: String): Message

  def withoutAllHeaders: Message

  val isChunked: Boolean

  def withChunked(chunked: Boolean): Message

}
