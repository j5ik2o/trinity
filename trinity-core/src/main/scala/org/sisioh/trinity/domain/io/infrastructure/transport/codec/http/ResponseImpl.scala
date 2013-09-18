package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import org.jboss.netty.handler.codec.http.{DefaultHttpResponse, HttpResponse => NettyResponse}
import org.sisioh.trinity.domain.io.transport.codec.http.{Response, ResponseStatus}
import org.sisioh.trinity.domain.io.transport.codec.http.ResponseStatus.{toNetty, toTrinity}
import org.sisioh.trinity.domain.io.transport.codec.http.Version
import org.sisioh.trinity.domain.io.transport.codec.http.Version.toNetty

/**
 * Netty Requestのラッパー。
 *
 * @param underlying
 */
private[trinity] case class ResponseImpl(override val underlying: NettyResponse)
  extends AbstractMessage(underlying) with Response {

   def this(version: Version.Value, status: ResponseStatus.Value) =
    this(new DefaultHttpResponse(version, status))
  
  protected def createMessage(message: AbstractMessage): this.type =
    new ResponseImpl(message.underlying.asInstanceOf[NettyResponse]).asInstanceOf[this.type]

  protected def mutateAsResponse(f: (NettyResponse) => Unit): this.type = {
    val cloned = createMessage(this)
    f(cloned.underlying)
    cloned
  }

  def status: ResponseStatus.Value = underlying.getStatus

  def withStatus(status: ResponseStatus.Value): this.type = mutateAsResponse {
    _.setStatus(status)
  }

}
