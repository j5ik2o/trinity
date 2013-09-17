package org.sisioh.trinity.domain.io.transport.codec.http

import org.jboss.netty.handler.codec.http.CookieEncoder
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

  val protocolVersion: Version.Value

  def withProtocolVersion(version: Version.Value): this.type

  val content: ChannelBuffer

  def withContent(content: ChannelBuffer): this.type

  def withHeader(name: String, value: Any): this.type

  def withHeader(name: String, values: Seq[_]): this.type

  def withoutHeader(name: String): this.type

  def withoutAllHeaders: this.type

  val isChunked: Boolean

  def withChunked(chunked: Boolean): this.type

  def withCookie(cookies: Seq[Cookie]): this.type = {
    val cookieEncoder = new CookieEncoder(true)
    cookies.foreach {
      xs =>
        cookieEncoder.addCookie(xs)
    }
    withHeader("Set-Cookie", cookieEncoder.encode)
  }

}
