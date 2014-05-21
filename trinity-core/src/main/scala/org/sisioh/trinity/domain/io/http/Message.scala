package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Message => FinagleMessage}
import org.jboss.netty.handler.codec.http.CookieDecoder
import org.jboss.netty.handler.codec.http.CookieEncoder
import org.sisioh.trinity.domain.io.buffer.{ChannelBuffers, ChannelBuffer}
import org.sisioh.trinity.domain.io.http.Cookie.toNetty
import org.sisioh.trinity.domain.io.http.Cookie.toTrinity
import scala.collection.JavaConverters.asScalaSetConverter
/**
 * Represents the trait for HTTP message.
 */
trait Message {

  /**
   * Creates a new instance from current instance with attributes.
   *
   * @param message current instance
   * @param attributes attributes
   * @return a new instance
   */
  protected def createInstance(message: this.type, attributes: Map[String, Any]): this.type

  /**
   * mutate function.
   *
   * @param f mutate function
   * @return new instance
   */
  protected def mutate(f: (this.type) => Unit): this.type = {
    val cloned = if (isMutable) {
      this.asInstanceOf[this.type]
    } else {
      createInstance(this, attributes)
    }
    f(cloned)
    cloned
  }

  /**
   * Gets whether mutate mode.
   */
  val isMutable: Boolean

  /**
   * Converts this instance to [[FinagleMessage]].
   */
  val toUnderlyingAsFinagle: FinagleMessage

  /**
   * Attribute values.
   */
  val attributes: Map[String, Any]

  /**
   * Creates a new instance with a new attributes to be added.
   *
   * @param attributes
   * @return a new instance
   */
  def withAttributes(attributes: Map[String, Any]): this.type

  /**
   * Creates a new instance with a new attributes to be added.
   *
   * @param attributes
   * @return A new instance
   */
  def withAttributes(attributes: (String, Any)*): this.type

  /**
   * Creates a new instance without all attributes.
   *
   * @return A new instance
   */
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

  /**
   * Gets whether this message type is request.
   *
   * @return True if it's request.
   */
  def isRequest: Boolean

  /**
   * Gets whether this message type is response.
   *
   * @return True if it's response.
   */
  def isResponse = !isRequest

  /**
   * Gets header value (as Option) this message has.
   *
   * @param name header name
   * @return If the header exists, then a header value in Some, else None.
   */
  def getHeader(name: HeaderName): Option[String]

  /**
   * Gets the header values (as Seq) this message has.
   *
   * @param name header name
   * @return If the header exists, then header values in Some, else None.
   */
  def getHeaders(name: HeaderName): Seq[String]

  /**
   * Gets headers as key-values this message has.
   *
   * @return If the header exist, then a header value in Some, else None.
   */
  def headers: Seq[(HeaderName, Any)]

  /**
   * Gets whether the header specified by the header name contains.
   *
   * @param name header name
   * @return True if it exist.
   */
  def containsHeader(name: HeaderName): Boolean

  /**
   * Gets header names this message has.
   *
   * @return header names
   */
  def headerNames: Set[HeaderName]

  /**
   * Gets HTTP protocol version this message has.
   *
   * @return HTTP protocol version
   */
  def protocolVersion: ProtocolVersion.Value

  /**
   * Creates a new instance with a new protocol version.
   *
   * @param version new protocol version.
   * @return A new instance
   */
  def withProtocolVersion(version: ProtocolVersion.Value): this.type

  /**
   * Gets the content (as HTTP message body) this message has.
   *
   * @return content
   */
  def content: ChannelBuffer

  /**
   * Creates a new instance with a new content.
   *
   * @param content content
   * @return A new instance
   */
  def withContent(content: ChannelBuffer): this.type

  /**
   * Gets the content as string this message has.
   *
   * @param charset Charset
   * @return content
   */
  def contentAsString(charset: Charset = Charsets.UTF_8): String = content.toString(charset)

  /**
   * Creates a new instance with a new content as string.
   *
   * @param body a new content
   * @param charset Charset
   * @return A new instance
   */
  def withContentAsString(body: String, charset: Charset = Charsets.UTF_8): this.type =
    withContent(ChannelBuffers.copiedBuffer(body, charset))

  /**
   * Creates a new instance with a new header to be added.
   *
   * @param name header name
   * @param value header value
   * @return A new instance
   */
  def withHeader(name: HeaderName, value: Any): this.type

  /**
   * Creates a new instance with a new header which has multiple values to be added.
   *
   * @param name header name
   * @param values header values
   * @return A new instance
   */
  def withHeader(name: HeaderName, values: Seq[_]): this.type

  /**
   * Creates a new instance with a new headers to be added.
   *
   * @param headers
   * @return
   */
  def withHeaders(headers: Seq[(HeaderName, Any)]): this.type = {
    headers.foldLeft(this) {
      (l, r) =>
        l.withHeader(r._1, r._2)
    }.asInstanceOf[this.type]
  }

  /**
   * Creates a new instance without a header specified.
   *
   * @param name header name
   * @return A new instance
   */
  def withoutHeader(name: HeaderName): this.type

  /**
   * Creates a new instance without all headers.
   *
   * @return A new instance
   */
  def withoutAllHeaders: this.type

  /**
   * Gets whether the chunked flag.
   *
   * @return True if it's chunked.
   */
  def isChunked: Boolean

  /**
   * Creates a new instance with a new chunked flag.
   *
   * @param chunked
   * @return A new instance
   */
  def withChunked(chunked: Boolean): this.type

  private val cookieHeaderName = if (isResponse) HeaderNames.SetCookie else HeaderNames.Cookie

  /**
   * Creates a new instance with cookies to be added.
   *
   * @param cookies [[Cookie]] collection
   * @return A new instance
   */
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

  /**
   * Gets cookies (as Seq).
   *
   * @return cookies
   */
  def cookies: Seq[Cookie] = {
    val decoder = new CookieDecoder()
    getHeader(cookieHeaderName).map {
      header =>
        decoder.decode(header).asScala.map(toTrinity).toSeq
    }.getOrElse(Seq.empty)
  }

  /**
   * Gets the Allow header value this message has.
   *
   * @return allow header value
   */
  def allow: Option[String] = toUnderlyingAsFinagle.allow

  /**
   * Creates a new instance with a new Allow header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withAllow(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.allow = value
  }

  /**
   * Gets the Authorization header value this message has.
   *
   * @return header value
   */
  def authorization: Option[String] = toUnderlyingAsFinagle.authorization

  /**
   * Creates a new instance with a new Authorization header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withAuthorization(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.authorization = value
  }

  /**
   * Gets the Cache-Control header value this message has.
   *
   * @return header value
   */
  def cacheControl: Option[String] = toUnderlyingAsFinagle.cacheControl

  /**
   * Creates a new instance with a new Cache-Control header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withCacheControl(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.cacheControl = value
  }

  /**
   * Gets the charset this message has
   *
   * @return charset
   */
  def charset: Option[String] = toUnderlyingAsFinagle.charset

  /**
   * Create a new instance with a new charset.
   *
   * @param value charset
   * @return A new instance
   */
  def withCharset(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.charset = value
  }

  /**
   * Gets the Content-Length header value this message has.
   *
   * @return content length
   */
  def contentLength: Option[Long] = toUnderlyingAsFinagle.contentLength

  /**
   * Creates a new instance with a new Content-Length header value.
   *
   * @param value content length
   * @return A new instance
   */
  def withContentLength(value: Long): this.type = mutate {
    _.toUnderlyingAsFinagle.contentLength = value
  }

  /**
   * Gets the Content-Type header value this message has.
   *
   * @return content type
   */
  def contentType: Option[String] = toUnderlyingAsFinagle.contentType

  /**
   * Creates a new instance with a new Content-Type header value.
   *
   * @param value content type
   * @return A new instance
   */
  def withContentType(value: ContentType): this.type = mutate {
    _.toUnderlyingAsFinagle.contentType = value.toString()
  }

  /**
   * Gets the Date header value this message has.
   *
   * @return header value
   */
  def date: Option[String] = toUnderlyingAsFinagle.date

  /**
   * Creates a new instance with a new Date header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withDate(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.date = value
  }

  /**
   * Gets the Expires header value this message has.
   *
   * @return header value
   */
  def expires: Option[String] = toUnderlyingAsFinagle.expires

  /**
   * Creates a new instance with a new Expires header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withExpire(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.expires = value
  }

  /**
   * Gets the Host header value this message has.
   *
   * @return header value
   */
  def host: Option[String] = toUnderlyingAsFinagle.host

  /**
   * Creates a new instance with a Host header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withHost(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.host = value
  }

  /**
   * Gets the Last-Modified header value this message has.
   *
   * @return header value
   */
  def lastModified: Option[String] = toUnderlyingAsFinagle.lastModified

  /**
   * Creates a new instance with a new Last-Mmodified header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withLastModified(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.lastModified = value
  }

  /**
   * Gets the Location header value this message has.
   *
   * @return header value
   */
  def location: Option[String] = toUnderlyingAsFinagle.location

  /**
   * Creates a new instance with a new Location header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withLocation(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.location = value
  }

  /**
   * Gets the media type in the Content-Type this message has.
   *
   * @return media type value
   */
  def mediaType: Option[String] = toUnderlyingAsFinagle.mediaType

  /**
   * Creates a new instance with a new media type.
   *
   * @param value media type
   * @return A new instance
   */
  def withMediaType(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.mediaType = value
  }

  /**
   * Gets the Referer header value this message has.
   *
   * @return
   */
  def referer: Option[String] = toUnderlyingAsFinagle.referer

  /**
   * Creates a new instance with a new Referer header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withReferer(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.referer = value
  }

  /**
   * Gets the Retry-After header value this message has.
   *
   * @return header value
   */
  def retryAfter: Option[String] = toUnderlyingAsFinagle.retryAfter

  /**
   * Creates a new instance with a new Retry-After header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withRetryAfter(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.retryAfter = value
  }

  /**
   * Gets the Server header value this message has.
   *
   * @return header value
   */
  def server: Option[String] = toUnderlyingAsFinagle.server

  def withServer(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.server = value
  }

  /**
   * Gets the User-Agent header value this message has.
   *
   * @return header value
   */
  def userAgent: Option[String] = toUnderlyingAsFinagle.userAgent

  /**
   * Creates a new instance with a new User-Agent header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withUserAgent(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.userAgent = value
  }

  /**
   * Gets the WWW-Authenticate header value this message has.
   *
   * @return header value
   */
  def wwwAuthenticate: Option[String] = toUnderlyingAsFinagle.wwwAuthenticate

  /**
   * Creates a new instance with a new WWW-Authentication header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withWwwAuthenticate(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.wwwAuthenticate = value
  }

  /**
   * Gets the X-Forward-For header value this message has.
   *
   * @return header value
   */
  def xForwardedFor: Option[String] = toUnderlyingAsFinagle.xForwardedFor

  /**
   * Creates a new instance with a new X-Forward-For header value.
   *
   * @param value header value
   * @return A new instance
   */
  def withXForwardedFor(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.xForwardedFor = value
  }

}
