package org.sisioh.trinity.domain.io.http

import org.jboss.netty.handler.codec.http.{Cookie => NettyCookie, DefaultCookie}
import scala.collection.JavaConverters.asScalaSetConverter

private[trinity]
case class CookieImpl(underlying: NettyCookie) extends Cookie {

  def this(name: String, value: String) =
    this(new DefaultCookie(name, value))

  private def mutate(f: (NettyCookie) => Unit): this.type = {
    val clone = CookieImpl(this)
    f(clone.underlying)
    clone.asInstanceOf[this.type]
  }

  val name = underlying.getName

  val value = underlying.getValue

  def withValue(value: String): this.type = mutate {
    _.setValue(value)
  }

  val domain = underlying.getDomain

  def withDomain(domain: String): this.type = mutate {
    _.setDomain(domain)
  }

  val path = underlying.getPath

  def withPath(path: String): this.type = mutate {
    _.setPath(path)
  }

  val comment = underlying.getComment

  def withComment(comment: String): this.type = mutate {
    _.setComment(comment)
  }

  val maxAge = underlying.getMaxAge

  def withMaxAge(maxAge: Int): this.type = mutate {
    _.setMaxAge(maxAge)
  }

  val version = underlying.getVersion

  def withVersion(version: Int): this.type = mutate {
    _.setVersion(version)
  }

  val isSecure = underlying.isSecure

  def withSecure(secure: Boolean): this.type = mutate {
    _.setSecure(secure)
  }

  val isHttpOnly = underlying.isHttpOnly

  def withHttpOnly(httpOnly: Boolean): this.type = mutate {
    _.setHttpOnly(httpOnly)
  }

  val commentUrl = underlying.getCommentUrl

  def withCommentUrl(commentUrl: String): this.type = mutate {
    _.setCommentUrl(commentUrl)
  }

  val isDiscard = underlying.isDiscard

  def withDiscard(discard: Boolean): this.type = mutate {
    _.setDiscard(discard)
  }

  val ports = underlying.getPorts.asScala.map(e => e.toInt).toSet

  def withPorts(ports: Int*): this.type = mutate {
    _.setPorts(ports: _*)
  }

}

