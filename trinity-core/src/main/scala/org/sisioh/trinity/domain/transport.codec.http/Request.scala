package org.sisioh.trinity.domain.transport.codec.http

import org.jboss.netty.handler.codec.http.{HttpRequest => NettyRequest, DefaultHttpRequest, HttpVersion, HttpMethod}
import org.sisioh.trinity.domain.infrastructure.netty3.transport.codec.http.RequestImpl

trait Request extends Message {

  val method: HttpMethod

  def withMethod(method: HttpMethod): Request

  val uri: String

  def withUri(uri: String): Request

}

object Request {

  private[trinity] def from(underlying: NettyRequest): Request =
    RequestImpl(underlying)

  def apply(httpVersion: HttpVersion, method: HttpMethod, uri: String): Request =
    from(new DefaultHttpRequest(httpVersion, method, uri))

}
