package org.sisioh.trinity.domain.io.infrastructure.netty3.transport.codec.http

import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse, HttpResponseStatus}
import org.sisioh.trinity.domain.io.transport.codec.http.Response
import org.sisioh.trinity.domain.io.infrastructure.netty3.transport.codec.http.AbstractMessage

/**
 * Netty Requestのラッパー。
 *
 * @param underlying
 */
private[trinity] case class ResponseImpl(override val underlying: NettyResponse)
  extends AbstractMessage(underlying) with Response {

  protected def createMessage(message: AbstractMessage): ResponseImpl =
    new ResponseImpl(message.underlying.asInstanceOf[NettyResponse])

  protected def mutateAsResponse(f: (NettyResponse) => Unit): Response = {
    val cloned = createMessage(this)
    f(cloned.underlying)
    cloned
  }

  val status: HttpResponseStatus = underlying.getStatus

  def withStatus(status: HttpResponseStatus): Response = mutateAsResponse {
    _.setStatus(status)
  }

}
