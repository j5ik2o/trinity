package org.sisioh.trinity.domain.mvc.http

import org.sisioh.trinity.domain.io.http.{Message => IOMessage}
import org.sisioh.trinity.domain.io.http.MessageProxy

import com.twitter.finagle.http.{Message => FinagleMessage}

trait Message extends IOMessage with MessageProxy {

  val toUnderlyingAsFinagle: FinagleMessage

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

  def withContentType(value: String): this.type = mutate {
    _.toUnderlyingAsFinagle.contentType = value
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
