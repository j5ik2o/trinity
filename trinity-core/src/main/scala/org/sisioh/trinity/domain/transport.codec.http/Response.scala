package org.sisioh.trinity.domain.transport.codec.http

import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse, HttpVersion, DefaultHttpResponse, HttpResponseStatus}
import org.sisioh.trinity.domain.infrastructure.netty3.http.ResponseImpl

trait Response extends Message {

  def getStatus: HttpResponseStatus

  def withStatus(status: HttpResponseStatus): Response

}

object Response {

  def from(underlying: NettyResponse): Response =
    ResponseImpl(underlying)

  def apply(version: HttpVersion, status: HttpResponseStatus): Response =
    from(new DefaultHttpResponse(version, status))

}
