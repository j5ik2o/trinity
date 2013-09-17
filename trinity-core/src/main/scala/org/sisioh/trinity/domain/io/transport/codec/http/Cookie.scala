package org.sisioh.trinity.domain.io.transport.codec.http

import com.twitter.finagle.http.{Cookie => FinagleCookie}
import org.jboss.netty.handler.codec.http.{Cookie => NettyCookie}
import org.sisioh.trinity.domain.io.infrastructure.transport.codec.http.CookieImpl
import scala.language.implicitConversions

trait Cookie {

  val name: String

  val value: String

  def withValue(value: String): this.type

  val domain: String

  def withDomain(domain: String): this.type

  val path: String

  def withPath(path: String): this.type

  val comment: String

  def withComment(comment: String): this.type

  val maxAge: Int

  def withMaxAge(maxAge: Int): this.type

  val version: Int

  def withVersion(version: Int): this.type

  val isSecure: Boolean

  def withSecure(secure: Boolean): this.type

  val isHttpOnly: Boolean

  def withHttpOnly(httpOnly: Boolean): this.type

  val commentUrl: String

  def withCommentUrl(commentUrl: String): this.type

  val isDiscard: Boolean

  def withDiscard(discard: Boolean): this.type

  val ports: Set[Int]

  def withPorts(ports: Int*): this.type

}

object Cookie {

  implicit def toFinagle(self: Cookie): FinagleCookie =
    new FinagleCookie(toNetty(self))

  implicit def toNetty(self: Cookie): NettyCookie =
    self match {
      case CookieImpl(underlying) => underlying
      case _ => throw new IllegalArgumentException()
    }

  implicit def toTrinity(underlying: NettyCookie): Cookie = CookieImpl(underlying)

}
