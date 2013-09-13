package org.sisioh.trinity.domain.io.infrastructure.netty3.transport.codec.http

import org.jboss.netty.handler.codec.http.{HttpRequest => NettyRequest, HttpMethod}
import org.sisioh.trinity.domain.io.transport.codec.http.Request

private[trinity] case class RequestImpl(override val underlying: NettyRequest)
  extends AbstractMessage(underlying) with Request {

  protected def createMessage(message: AbstractMessage): RequestImpl =
    new RequestImpl(message.underlying.asInstanceOf[NettyRequest])

  protected def mutateAsRequest(f: (NettyRequest) => Unit): Request = {
    val cloned = createMessage(this)
    f(cloned.underlying)
    cloned
  }

  val method: HttpMethod = underlying.getMethod

  def withMethod(method: HttpMethod): Request = mutateAsRequest {
    _.setMethod(method)
  }

  val uri: String = underlying.getUri

  def withUri(uri: String): Request = mutateAsRequest {
    _.setUri(uri)
  }
}
