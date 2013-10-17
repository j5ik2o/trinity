package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.{HttpRequest => NettyRequest}
import scala.language.implicitConversions
import org.sisioh.trinity.domain.io.infrastructure.http.RequestImpl

trait Request extends Message {

  val netty: NettyRequest

  val method: Method.Value

  def withMethod(method: Method.Value): this.type

  val uri: String

  def withUri(uri: String): this.type

}

object Request {

  private[http] implicit def toFinagle(self: Request): FinagleRequest =
    FinagleRequest(self.netty)

  private[http] implicit def toNetty(self: Request): NettyRequest =
    self.netty

  private[domain] implicit def toTrinity(underlying: NettyRequest): Request =
    RequestImpl(underlying)

  def apply(method: Method.Value, uri: String, version: Version.Value = Version.Http11): Request =
    new RequestImpl(method, uri, version = version)

}
