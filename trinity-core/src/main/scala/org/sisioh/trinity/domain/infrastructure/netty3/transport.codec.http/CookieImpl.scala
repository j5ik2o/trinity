package org.sisioh.trinity.domain.infrastructure.netty3.transport.codec.http

import org.jboss.netty.handler.codec.http.{Cookie => NettyCookie, DefaultCookie}
import scala.collection.JavaConverters._
import org.sisioh.trinity.domain.transport.codec.http.Cookie

private[trinity] case class CookieImpl(underlying: NettyCookie) extends Cookie {

  private def mutate(f: (NettyCookie) => Unit): Cookie = {
    val clone = CookieImpl(this)
    f(clone.underlying)
    clone
  }

  val name: String = underlying.getName

  val value: String = underlying.getValue

  def withValue(value: String): Cookie = mutate {
    _.setValue(value)
  }

  val domain: String = underlying.getDomain

  def withDomain(domain: String): Cookie = mutate {
    _.setDomain(domain)
  }

  val path: String = underlying.getPath

  def withPath(path: String): Cookie = mutate {
    _.setPath(path)
  }

  val comment: String = underlying.getComment

  def withComment(comment: String): Cookie = mutate {
    _.setComment(comment)
  }

  val maxAge: Int = underlying.getMaxAge

  def withMaxAge(maxAge: Int): Cookie = mutate {
    _.setMaxAge(maxAge)
  }

  val version: Int = underlying.getVersion

  def withVersion(version: Int): Cookie = mutate {
    _.setVersion(version)
  }

  val isSecure: Boolean = underlying.isSecure

  def withSecure(secure: Boolean): Cookie = mutate {
    _.setSecure(secure)
  }

  val isHttpOnly: Boolean = underlying.isHttpOnly

  def withHttpOnly(httpOnly: Boolean): Cookie = mutate {
    _.setHttpOnly(httpOnly)
  }

  val commentUrl: String = underlying.getCommentUrl

  def withCommentUrl(commentUrl: String): Cookie = mutate {
    _.setCommentUrl(commentUrl)
  }

  val isDiscard: Boolean = underlying.isDiscard

  def withDiscard(discard: Boolean): Cookie = mutate {
    _.setDiscard(discard)
  }

  val ports: Set[Int] = underlying.getPorts.asScala.map(e => e.toInt).toSet

  def withPorts(ports: Int*): Cookie = mutate {
    _.setPorts(ports: _*)
  }

}

object CookieImpl {

  private def apply(cookie: CookieImpl): CookieImpl =
    CookieImpl(new DefaultCookie(cookie.underlying.getName, cookie.underlying.getValue))

}