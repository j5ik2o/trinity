package org.sisioh.trinity.domain.mvc.http

import org.sisioh.trinity.domain.io.http.{Response => IOResponse, AbstractResponseProxy, ResponseStatus, ProtocolVersion}

private[http]
class ResponseImpl(override val underlying: IOResponse)
  extends AbstractResponseProxy(underlying) with Response {

  def this(responseStatus: ResponseStatus.Value = ResponseStatus.Ok,
           protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11) =
    this(IOResponse(responseStatus, protocolVersion))

  val toUnderlyingAsFinagle = underlying.toUnderlyingAsFinagle

  protected def createInstance(message: this.type, attributes: Map[String, Any]): this.type =
    new ResponseImpl(message.underlying.withAttributes(attributes)).asInstanceOf[this.type]

  val attributes: Map[String, Any] = underlying.attributes

  def withAttributes(_attributes: Map[String, Any]): this.type =
    new ResponseImpl(underlying.withAttributes(_attributes)).asInstanceOf[this.type]

  def withAttributes(_attributes: (String, Any)*): this.type =
    new ResponseImpl(underlying.withAttributes(_attributes: _*)).asInstanceOf[this.type]

  def withoutAllAttributes(): this.type =
    new ResponseImpl(underlying.withoutAllAttributes()).asInstanceOf[this.type]

}
