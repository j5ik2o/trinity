package org.sisioh.trinity.domain.io.infrastructure.http

import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse, DefaultHttpResponse}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.ResponseStatus.{toNetty, toTrinity}
import org.sisioh.trinity.domain.io.http.Version.toNetty
import org.sisioh.trinity.domain.io.http.{Cookie, Version, Response, ResponseStatus}

/**
 * Netty Requestのラッパー。
 *
 * @param netty
 */
private[trinity]
case class ResponseImpl(override val netty: NettyResponse)
  extends AbstractMessage(netty) with Response {

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

  def isRequest: Boolean = false

  protected def createInstance(message: AbstractMessage): this.type =
    new ResponseImpl(message.netty.asInstanceOf[NettyResponse]).asInstanceOf[this.type]

  protected def mutateAsResponse(f: (NettyResponse) => Unit): this.type = {
    val cloned = createInstance(this)
    f(cloned.netty)
    cloned
  }

  val status: ResponseStatus.Value = netty.getStatus

  def withStatus(status: ResponseStatus.Value): this.type = mutateAsResponse {
    _.setStatus(status)
  }

}
