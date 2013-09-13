package org.sisioh.trinity.domain.infrastructure.netty3.transport.codec.http

import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse, HttpResponseStatus}
import org.sisioh.trinity.domain.transport.codec.http.Response

private[trinity] case class ResponseImpl(override val underlying: NettyResponse)
  extends AbstractMessage(underlying) with Response {

  protected def createMessage(message: AbstractMessage): ResponseImpl =
    new ResponseImpl(message.underlying.asInstanceOf[NettyResponse])

  protected def mutateAsResponse(f: (NettyResponse) => Unit): Response = {
    val cloned = createMessage(this)
    f(cloned.underlying)
    cloned
  }

  def getStatus: HttpResponseStatus = underlying.getStatus

  def withStatus(status: HttpResponseStatus): Response = mutateAsResponse {
    _.setStatus(status)
  }

}
