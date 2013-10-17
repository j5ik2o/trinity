package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import org.jboss.netty.handler.codec.http.{DefaultHttpRequest, HttpRequest => NettyRequest}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.transport.codec.http.Method.{toNetty, toTrintiy}
import org.sisioh.trinity.domain.io.transport.codec.http.Version.toNetty
import org.sisioh.trinity.domain.io.transport.codec.http.{Cookie, Request, Version, Method}

private[trinity]
case class RequestImpl(override val netty: NettyRequest)
  extends AbstractMessage(netty) with Request {

  def isRequest: Boolean = true

  def this(method: Method.Value,
           uri: String,
           headers: Seq[(String, Any)] = Seq.empty,
           cookies: Seq[Cookie] = Seq.empty,
           content: ChannelBuffer = ChannelBuffer.empty,
           version: Version.Value = Version.Http11) = {
    this(new DefaultHttpRequest(version, method, uri))
    setHeaders(headers)
    setCookies(cookies)
    setContent(content)
  }

  protected def createInstance(message: AbstractMessage): this.type =
    new RequestImpl(message.netty.asInstanceOf[NettyRequest]).asInstanceOf[this.type]

  protected def mutateAsRequest(f: (NettyRequest) => Unit): this.type = {
    val cloned = createInstance(this)
    f(cloned.netty)
    cloned
  }

  val method: Method.Value = netty.getMethod

  def withMethod(method: Method.Value): this.type = mutateAsRequest {
    _.setMethod(method)
  }

  val uri: String = netty.getUri

  def withUri(uri: String): this.type = mutateAsRequest {
    _.setUri(uri)
  }

}
