package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Response => FinagleResponse}
import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse}
import scala.language.implicitConversions

import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse, DefaultHttpResponse}
import org.sisioh.trinity.domain.io.infrastructure.http.ResponseImpl

trait Response extends Message {

  val finagle : FinagleResponse

  def status: ResponseStatus.Value

  def withStatus(status: ResponseStatus.Value): this.type

}

object Response {

  def apply(underlying: FinagleResponse): Response =
    ResponseImpl(underlying)

  def apply(status: ResponseStatus.Value = ResponseStatus.Ok, version: Version.Value = Version.Http11): Response =
    new ResponseImpl(status, version = version)

}
