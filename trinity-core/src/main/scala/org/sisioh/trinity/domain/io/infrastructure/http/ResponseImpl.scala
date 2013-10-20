package org.sisioh.trinity.domain.io.infrastructure.http

import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.ResponseStatus.{toNetty, toTrinity}
import org.sisioh.trinity.domain.io.http.{Cookie, Version, Response, ResponseStatus}

import com.twitter.finagle.http.{Response => FinagleResponse}

/**
 * Netty Requestのラッパー。
 *
 * @param finagle
 */
private[trinity]
case class ResponseImpl(override val finagle: FinagleResponse)
  extends AbstractMessage(finagle) with Response {

  def this(status: ResponseStatus.Value,
           headers: Seq[(String, Any)] = Seq.empty,
           cookies: Seq[Cookie] = Seq.empty,
           content: ChannelBuffer = ChannelBuffer.empty,
           version: Version.Value = Version.Http11) = {
    this(FinagleResponse(version, status))
    setHeaders(headers)
    setCookies(cookies)
    setContent(content)
  }

  def isRequest: Boolean = false

  protected def createInstance(message: AbstractMessage): this.type =
    new ResponseImpl(message.finagle.asInstanceOf[FinagleResponse]).asInstanceOf[this.type]

  protected def mutateAsResponse(f: (NettyResponse) => Unit): this.type = {
    val cloned = createInstance(this)
    f(cloned.finagle)
    cloned
  }

  val status: ResponseStatus.Value = finagle.getStatus()

  def withStatus(status: ResponseStatus.Value): this.type = mutateAsResponse {
    _.setStatus(status)
  }

}
