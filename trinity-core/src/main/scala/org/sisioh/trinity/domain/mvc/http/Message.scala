package org.sisioh.trinity.domain.mvc.http

import com.twitter.finagle.http.{Message => FinagleMessage}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffers
import org.sisioh.trinity.domain.io.http.{Message => IOMessage, MessageProxy, CharsetUtil}

trait Message extends IOMessage with MessageProxy {

  val finagle: FinagleMessage

  def contentAsString: String = content.toString

  def withContentAsString(body: String): this.type =
    withContent(ChannelBuffers.copiedBuffer(body, CharsetUtil.UTF_8))

  def allow: Option[String] = finagle.allow

  def withAllow(value: String): this.type = mutate {
    _.finagle.allow = value
  }

  def authorization: Option[String] = finagle.authorization

  def withAuthorization(value: String): this.type = mutate {
    _.finagle.authorization = value
  }

  def cacheControl: Option[String] = finagle.cacheControl

  def withCacheControl(value: String): this.type = mutate {
    _.finagle.cacheControl = value
  }

  def charset: Option[String] = finagle.charset

  def withCharset(value: String): this.type = mutate {
    _.finagle.charset = value
  }

  def contentLength: Option[Long] = finagle.contentLength

  def withContentLength(value: Long): this.type = mutate {
    _.finagle.contentLength = value
  }

  def contentType: Option[String] = finagle.contentType

  def withContentType(value: String): this.type = mutate {
    _.finagle.contentType = value
  }

  def date: Option[String] = finagle.date

  def withDate(value: String): this.type = mutate {
    _.finagle.date = value
  }

  def expires: Option[String] = finagle.expires

  def withExpire(value: String): this.type = mutate {
    _.finagle.expires = value
  }

  def host: Option[String] = finagle.host

  def withHost(value: String): this.type = mutate {
    _.finagle.host = value
  }

  def lastModified: Option[String] = finagle.lastModified

  def withLastModified(value: String): this.type = mutate {
    _.finagle.lastModified = value
  }

  def location: Option[String] = finagle.location

  def withLocation(value: String): this.type = mutate {
    _.finagle.location = value
  }

  def mediaType: Option[String] = finagle.mediaType

  def withMediaType(value: String): this.type = mutate {
    _.finagle.mediaType = value
  }

  def referer: Option[String] = finagle.referer

  def withReferer(value: String): this.type = mutate {
    _.finagle.referer = value
  }

  def retryAfter: Option[String] = finagle.retryAfter

  def withRetryAfter(value: String): this.type = mutate {
    _.finagle.retryAfter = value
  }

  def server: Option[String] = finagle.server

  def withServer(value: String): this.type = mutate {
    _.finagle.server = value
  }

  def userAgent: Option[String] = finagle.userAgent

  def withUserAgent(value: String): this.type = mutate {
    _.finagle.userAgent = value
  }

  def wwwAuthenticate: Option[String] = finagle.wwwAuthenticate

  def withWwwAuthenticate(value: String): this.type = mutate {
    _.finagle.wwwAuthenticate = value
  }

  def xForwardedFor: Option[String] = finagle.xForwardedFor

  def withXForwardedFor(value: String): this.type = mutate {
    _.finagle.xForwardedFor = value
  }

}
