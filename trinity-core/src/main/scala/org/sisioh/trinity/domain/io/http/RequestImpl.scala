package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.{HttpRequest => NettyRequest}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.Methods.toNetty
import org.sisioh.trinity.domain.io.http.Methods.toTrintiy
import org.sisioh.trinity.domain.io.http.ProtocolVersion.toNetty


private[trinity]
case class RequestImpl(override val toUnderlyingAsFinagle: FinagleRequest,
                       attributes: Map[String, Any] = Map.empty[String, Any],
                       isMutable: Boolean = false)
  extends AbstractMessage(toUnderlyingAsFinagle) with Request {

  def isRequest: Boolean = true

  def this(method: Methods.Value,
           uri: String,
           headers: Seq[(HeaderName, Any)] = Seq.empty,
           cookies: Seq[Cookie] = Seq.empty,
           attributes: Map[String, Any] = Map.empty[String, Any],
           content: ChannelBuffer = ChannelBuffer.empty,
           isMutable: Boolean = false,
           protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11) = {
    this(FinagleRequest(protocolVersion, method, uri), attributes, isMutable)
    setHeaders(headers)
    setCookies(cookies)
    setContent(content)
  }

  protected def createInstance(message: this.type, attributes: Map[String, Any]): this.type =
    new RequestImpl(message.toUnderlyingAsFinagle.asInstanceOf[FinagleRequest], attributes).asInstanceOf[this.type]

  protected def mutateAsRequest(f: (NettyRequest) => Unit): this.type = {
    val cloned = if (!isMutable) {
      createInstance(this, attributes)
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
