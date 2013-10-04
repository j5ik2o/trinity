package org.sisioh.trinity.domain.mvc.http

import org.sisioh.trinity.domain.io.infrastructure.transport.codec.http.AbstractResponseProxy
import org.sisioh.trinity.domain.io.transport.codec.http
import org.sisioh.trinity.domain.io.transport.codec.http.{ResponseStatus, Version}

class ResponseImpl(override val underlying: http.Response)
  extends AbstractResponseProxy(underlying) with Response {

  def this(version: Version.Value, status: ResponseStatus.Value) =
    this(http.Response(version, status))

  protected def createInstance(message: this.type): this.type =
    new ResponseImpl(message.underlying).asInstanceOf[this.type]

}
