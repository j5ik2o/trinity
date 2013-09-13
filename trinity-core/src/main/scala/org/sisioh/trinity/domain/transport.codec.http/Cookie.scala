package org.sisioh.trinity.domain.transport.codec.http

import org.jboss.netty.handler.codec.http.{Cookie => NettyCookie}
import org.sisioh.trinity.domain.infrastructure.netty3.transport.codec.http.CookieImpl

trait Cookie {

  val name: String

  val value: String

  def withValue(value: String): Cookie

  val domain: String

  def withDomain(domain: String): Cookie

  val path: String

  def withPath(path: String): Cookie

  val comment: String

  def withComment(comment: String): Cookie

  val maxAge: Int

  def withMaxAge(maxAge: Int): Cookie

  val version: Int

  def withVersion(version: Int): Cookie

  val isSecure: Boolean

  def withSecure(secure: Boolean): Cookie

  val isHttpOnly: Boolean

  def withHttpOnly(httpOnly: Boolean): Cookie

  val commentUrl: String

  def withCommentUrl(commentUrl: String): Cookie

  val isDiscard: Boolean

  def withDiscard(discard: Boolean): Cookie

  val ports: Set[Int]

  def withPorts(ports: Int*): Cookie

}

object Cookie {

  def from(underlying: NettyCookie): Cookie = CookieImpl(underlying)

}
