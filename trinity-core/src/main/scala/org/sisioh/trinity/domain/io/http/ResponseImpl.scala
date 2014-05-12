package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Response => FinagleResponse}
import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.ResponseStatus.{toNetty, toTrinity}

/**
 * Represents a implementation class for [[Response]].
 *
 * @param toUnderlyingAsFinagle
 */
private[trinity]
case class ResponseImpl(override val toUnderlyingAsFinagle: FinagleResponse,
                        attributes: Map[String, Any] = Map.empty[String, Any],
                        isMutable: Boolean = false)
  extends AbstractMessage(toUnderlyingAsFinagle) with Response {

  /**
   * Auxiliary constructor.
   *
   * @param status
   * @param headers
   * @param cookies
   * @param attributes
   * @param content
   * @param isMutable
   * @param protocolVersion
   */
  def this(status: ResponseStatus.Value,
           headers: Seq[(HeaderName, Any)] = Seq.empty,
           cookies: Seq[Cookie] = Seq.empty,
           attributes: Map[String, Any] = Map.empty[String, Any],
           content: ChannelBuffer = ChannelBuffer.empty,
           isMutable: Boolean = false,
           protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11) = {
    this(FinagleResponse(protocolVersion, status), attributes, isMutable)
    setHeaders(headers)
    setCookies(cookies)
    setContent(content)
  }

  override def isRequest: Boolean = false

  override protected def createInstance(message: this.type, attributes: Map[String, Any]): this.type =
    new ResponseImpl(message.toUnderlyingAsFinagle.asInstanceOf[FinagleResponse], attributes).asInstanceOf[this.type]

  protected def mutateAsResponse(f: (NettyResponse) => Unit): this.type = {
    val cloned = if (!isMutable) {
      createInstance(this, attributes)
    } else {
      this
    }
    f(cloned.toUnderlyingAsFinagle)
    cloned.asInstanceOf[this.type]
  }

  override def responseStatus: ResponseStatus.Value = toUnderlyingAsFinagle.getStatus()

  override def withResponseStatus(status: ResponseStatus.Value): this.type = mutateAsResponse {
    _.setStatus(status)
  }

}
