package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Message => FinagleMessage}
import org.jboss.netty.handler.codec.http.CookieDecoder
import org.jboss.netty.handler.codec.http.CookieEncoder
import org.sisioh.trinity.domain.io.buffer.{ChannelBuffers, ChannelBuffer}
import org.sisioh.trinity.domain.io.http.Cookie.toNetty
import org.sisioh.trinity.domain.io.http.Cookie.toTrinity
import scala.collection.JavaConverters.asScalaSetConverter


/**
 * HTTPのメッセージを表すトレイト。
 */
trait Message {

  val isMutable: Boolean = false

  val toUnderlyingAsFinagle: FinagleMessage

  override def toString = Seq(
    s"protocolVersion = $protocolVersion",
    s"headers = $headers",
    s"content = $content"
  ).mkString("Message(", ", ", ")")

  override def equals(obj: Any): Boolean = obj match {
    case that: Message =>
      protocolVersion == that.protocolVersion &&
        headers == that.headers &&
        content == that.content
    case _ => false
  }

  override def hashCode: Int =
    31 * (protocolVersion.## + headers.## + content.##)

  def isRequest: Boolean

  def isResponse = !isRequest

  def getHeader(name: HeaderName): Option[String]

  def getHeaders(name: HeaderName): Seq[String]

  def headers: Seq[(HeaderName, Any)]

  def containsHeader(name: HeaderName): Boolean

  def headerNames: Set[HeaderName]

  def protocolVersion: ProtocolVersion.Value

  def withProtocolVersion(version: ProtocolVersion.Value): this.type

  def content: ChannelBuffer

  def withContent(content: ChannelBuffer): this.type

  def contentAsString(charset: Charset = Charsets.UTF_8): String = content.toString(charset)

  def withContentAsString(body: String, charset: Charset = Charsets.UTF_8): this.type =
    withContent(ChannelBuffers.copiedBuffer(body, charset))

  def withHeader(name: HeaderName, value: Any): this.type

  def withHeader(name: HeaderName, values: Seq[_]): this.type

  def withHeaders(headers: Seq[(HeaderName, Any)]): this.type = {
    headers.foldLeft(this) {
      (l, r) =>
        l.withHeader(r._1, r._2)
    }.asInstanceOf[this.type]
  }

  def withoutHeader(name: HeaderName): this.type

  def withoutAllHeaders: this.type

  def isChunked: Boolean

  def withChunked(chunked: Boolean): this.type

  private val cookieHeaderName = if (isResponse) HeaderNames.SetCookie else HeaderNames.Cookie

  def withCookies(cookies: Seq[Cookie]): this.type = {
    val cookieEncoder = new CookieEncoder(true)
    cookies.foreach {
      xs =>
        cookieEncoder.addCookie(xs)
    }
    withHeader(cookieHeaderName, cookieEncoder.encode)
  }

  def cookies: Seq[Cookie] = {
    val decoder = new CookieDecoder()
    val header = getHeader(cookieHeaderName).get
    decoder.decode(header).asScala.map(toTrinity).toSeq
  }

}
