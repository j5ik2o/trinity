package org.sisioh.trinity.domain.io.transport.codec.http

import scala.language.implicitConversions

import org.jboss.netty.handler.codec.http.{HttpRequest => NettyRequest}
import org.sisioh.trinity.domain.io.infrastructure.transport.codec.http.RequestImpl

import com.twitter.finagle.http.{Request => FinagleRequest}

trait Request extends Message {

  val method: Method.Value

  def withMethod(method: Method.Value): this.type

  val uri: String

  def withUri(uri: String): this.type

}

object Request {

  implicit def toFinagle(self: Request): FinagleRequest =
    FinagleRequest(toNetty(self))

  implicit def toNetty(self: Request): NettyRequest =
    self match {
      case RequestImpl(underlying) => underlying
      case _ => throw new IllegalArgumentException()
    }

  implicit def toTrinity(underlying: NettyRequest): Request =
    RequestImpl(underlying)

  def apply(httpVersion: Version.Value, method: Method.Value, uri: String): Request =
    new RequestImpl(httpVersion, method, uri)

}
