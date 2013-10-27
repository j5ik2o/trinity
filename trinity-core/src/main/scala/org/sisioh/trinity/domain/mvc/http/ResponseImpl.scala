package org.sisioh.trinity.domain.mvc.http

import org.sisioh.trinity.domain.io.http.{Response => IOResponse}
import org.sisioh.trinity.domain.io.http.{ResponseStatus, ProtocolVersion}
import org.sisioh.trinity.domain.io.infrastructure.http.AbstractResponseProxy

private[http]
class ResponseImpl(override val underlying: IOResponse)
  extends AbstractResponseProxy(underlying) with Response {

  def this(responseStatus: ResponseStatus.Value = ResponseStatus.Ok,
           protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11) =
    this(IOResponse(responseStatus, protocolVersion))

  val toUnderlyingAsFinagle = underlying.toUnderlyingAsFinagle

  protected def createInstance(message: this.type): this.type =
    new ResponseImpl(message.underlying).asInstanceOf[this.type]

}
