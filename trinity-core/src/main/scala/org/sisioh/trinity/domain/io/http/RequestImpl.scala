package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.{HttpRequest => NettyRequest}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.Methods.toNetty
import org.sisioh.trinity.domain.io.http.Methods.toTrintiy
import org.sisioh.trinity.domain.io.http.ProtocolVersion.toNetty
import org.sisioh.trinity.domain.io.http._


private[trinity]
class RequestImpl(override val toUnderlyingAsFinagle: FinagleRequest)
  extends AbstractMessage(toUnderlyingAsFinagle) with Request {

  def isRequest: Boolean = true

  def this(method: Methods.Value,
           uri: String,
           headers: Seq[(String, Any)] = Seq.empty,
           cookies: Seq[Cookie] = Seq.empty,
           content: ChannelBuffer = ChannelBuffer.empty,
           protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11) = {
    this(FinagleRequest(protocolVersion, method, uri))
    setHeaders(headers)
    setCookies(cookies)
    setContent(content)
  }

  protected def createInstance(message: AbstractMessage): this.type =
    new RequestImpl(message.toUnderlyingAsFinagle.asInstanceOf[FinagleRequest]).asInstanceOf[this.type]

  protected def mutateAsRequest(f: (NettyRequest) => Unit): this.type = {
    val cloned = if (!isMutable) {
      createInstance(this)
    } else {
      this
    }
    f(cloned.toUnderlyingAsFinagle)
    cloned.asInstanceOf[this.type]
  }

  def response: Response = Response(toUnderlyingAsFinagle.response)

  def method: Methods.Value = toUnderlyingAsFinagle.getMethod()

  def withMethod(method: Methods.Value): this.type = mutateAsRequest {
    _.setMethod(method)
  }

  def uri = toUnderlyingAsFinagle.getUri()

  def withUri(uri: String): this.type = mutateAsRequest {
    _.setUri(uri)
  }

}
