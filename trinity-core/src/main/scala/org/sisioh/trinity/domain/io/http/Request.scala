package org.sisioh.trinity.domain.io.http

import scala.language.implicitConversions

import org.jboss.netty.handler.codec.http.{HttpRequest => NettyRequest}
import org.sisioh.trinity.domain.io.infrastructure.http.RequestImpl

import com.twitter.finagle.http.{Request => FinagleRequest}

trait Request extends Message {

  val finagle: FinagleRequest

  val method: Method.Value

  def withMethod(method: Method.Value): this.type

  val uri: String

  def withUri(uri: String): this.type

}

object Request {

  private[http] implicit def toFinagle(self: Request): FinagleRequest =
    self.finagle

  private[http] implicit def toNetty(self: Request): NettyRequest =
    self.finagle

  def apply(method: Method.Value, uri: String, version: Version.Value = Version.Http11): Request =
    new RequestImpl(method, uri, version = version)

  def apply(request: FinagleRequest): Request =
    new RequestImpl(request)
}
