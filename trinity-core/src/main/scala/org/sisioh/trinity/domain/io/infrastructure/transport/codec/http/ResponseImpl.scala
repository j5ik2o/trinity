package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse, DefaultHttpResponse}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.transport.codec.http.ResponseStatus.{toNetty, toTrinity}
import org.sisioh.trinity.domain.io.transport.codec.http.Version.toNetty
import org.sisioh.trinity.domain.io.transport.codec.http.{Cookie, Version, Response, ResponseStatus}

/**
 * Netty Requestのラッパー。
 *
 * @param underlying
 */
private[trinity]
case class ResponseImpl(override val underlying: NettyResponse)
  extends AbstractMessage(underlying) with Response {

  def this(status: ResponseStatus.Value,
           headers: Seq[(String, Any)] = Seq.empty,
           cookies: Seq[Cookie] = Seq.empty,
           content: ChannelBuffer = ChannelBuffer.empty,
           version: Version.Value = Version.Http11) = {
    this(new DefaultHttpResponse(version, status))
    setHeaders(headers)
    setCookies(cookies)
    setContent(content)
  }

  protected def createInstance(message: AbstractMessage): this.type =
    new ResponseImpl(message.underlying.asInstanceOf[NettyResponse]).asInstanceOf[this.type]

  protected def mutateAsResponse(f: (NettyResponse) => Unit): this.type = {
    val cloned = createInstance(this)
    f(cloned.underlying)
    cloned
  }

  val status: ResponseStatus.Value = underlying.getStatus

  def withStatus(status: ResponseStatus.Value): this.type = mutateAsResponse {
    _.setStatus(status)
  }

}
