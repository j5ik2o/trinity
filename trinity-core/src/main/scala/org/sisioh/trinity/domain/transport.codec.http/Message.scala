package org.sisioh.trinity.domain.transport.codec.http

import org.sisioh.trinity.domain.buffer.ChannelBuffer
import org.jboss.netty.handler.codec.http.HttpVersion

/**
 * HTTPのメッセージを表すトレイト。
 */
trait Message {

  def getHeader(name: String): String

  def getHeaders(name: String): Seq[String]

  def getHeaders: Seq[(String, String)]

  def containsHeader(name: String): Boolean

  def getHeaderNames: Set[String]

  def getProtocolVersion: HttpVersion

  def withProtocolVersion(version: HttpVersion): Message

  def getContent: ChannelBuffer

  def withContent(content: ChannelBuffer): Message

  def withHeader(name: String, value: Any): Message

  def withHeader(name: String, values: Seq[_]): Message

  def withoutHeader(name: String): Message

  def withoutAllHeaders: Message

  def isChunked: Boolean

  def withChunked(chunked: Boolean): Message

}
