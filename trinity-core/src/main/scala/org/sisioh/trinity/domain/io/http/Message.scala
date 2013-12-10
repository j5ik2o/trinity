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

  protected def createInstance(message: this.type, attributes: Map[String, Any]): this.type

  protected def mutate(f: (this.type) => Unit): this.type = {
    val cloned = if (isMutable) {
      this.asInstanceOf[this.type]
    } else {
      createInstance(this, attributes)
    }
    f(cloned)
    cloned
  }

  val isMutable: Boolean

  val toUnderlyingAsFinagle: FinagleMessage

  val attributes: Map[String, Any]

  def withAttributes(attributes: Map[String, Any]): this.type

  def withAttributes(attributes: (String, Any)*): this.type

  def withoutAllAttributes(): this.type

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
    if (!cookies.isEmpty) {
      withHeader(cookieHeaderName, cookieEncoder.encode)
    } else this
  }

  def cookies: Seq[Cookie] = {
    val decoder = new CookieDecoder()
    getHeader(cookieHeaderName).map {
      header =>
        decoder.decode(header).asScala.map(toTrinity).toSeq
    }.getOrElse(Seq.empty)
  }

  def allow: Option[String] = toUnderlyingAsFinagle.allow

  def withAllow(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.allow = value
  }

  def authorization: Option[String] = toUnderlyingAsFinagle.authorization

  def withAuthorization(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.authorization = value
  }

  def cacheControl: Option[String] = toUnderlyingAsFinagle.cacheControl

  def withCacheControl(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.cacheControl = value
  }

  def charset: Option[String] = toUnderlyingAsFinagle.charset

  def withCharset(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.charset = value
  }

  def contentLength: Option[Long] = toUnderlyingAsFinagle.contentLength

  def withContentLength(value: Long): this.type = mutate {
    _.toUnderlyingAsFinagle.contentLength = value
  }

  def contentType: Option[String] = toUnderlyingAsFinagle.contentType

  def withContentType(value: ContentType): this.type = mutate {
    _.toUnderlyingAsFinagle.contentType = value.toString()
  }

  def date: Option[String] = toUnderlyingAsFinagle.date

  def withDate(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.date = value
  }

  def expires: Option[String] = toUnderlyingAsFinagle.expires

  def withExpire(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.expires = value
  }

  def host: Option[String] = toUnderlyingAsFinagle.host

  def withHost(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.host = value
  }

  def lastModified: Option[String] = toUnderlyingAsFinagle.lastModified

  def withLastModified(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.lastModified = value
  }

  def location: Option[String] = toUnderlyingAsFinagle.location

  def withLocation(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.location = value
  }

  def mediaType: Option[String] = toUnderlyingAsFinagle.mediaType

  def withMediaType(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.mediaType = value
  }

  def referer: Option[String] = toUnderlyingAsFinagle.referer

  def withReferer(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.referer = value
  }

  def retryAfter: Option[String] = toUnderlyingAsFinagle.retryAfter

  def withRetryAfter(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.retryAfter = value
  }

  def server: Option[String] = toUnderlyingAsFinagle.server

  def withServer(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.server = value
  }

  def userAgent: Option[String] = toUnderlyingAsFinagle.userAgent

  def withUserAgent(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.userAgent = value
  }

  def wwwAuthenticate: Option[String] = toUnderlyingAsFinagle.wwwAuthenticate

  def withWwwAuthenticate(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.wwwAuthenticate = value
  }

  def xForwardedFor: Option[String] = toUnderlyingAsFinagle.xForwardedFor

  def withXForwardedFor(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.xForwardedFor = value
  }

}
