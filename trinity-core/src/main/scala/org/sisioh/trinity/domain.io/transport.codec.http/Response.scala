package org.sisioh.trinity.domain.io.transport.codec.http

import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse, HttpVersion, DefaultHttpResponse, HttpResponseStatus}
import org.sisioh.trinity.domain.io.infrastructure.netty3.transport.codec.http.ResponseImpl

trait Response extends Message {

  val status: HttpResponseStatus

  def withStatus(status: HttpResponseStatus): Response

}

object Response {

  private[trinity] def from(underlying: NettyResponse): Response =
    ResponseImpl(underlying)

  def apply(version: HttpVersion, status: HttpResponseStatus): Response =
    from(new DefaultHttpResponse(version, status))

}
