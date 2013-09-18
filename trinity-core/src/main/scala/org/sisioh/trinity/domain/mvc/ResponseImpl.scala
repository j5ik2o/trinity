package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.infrastructure.transport.codec.http.AbstractResponseProxy
import org.sisioh.trinity.domain.io.transport.codec.http

case class ResponseImpl(override val underlying: http.Response)
  extends AbstractResponseProxy(underlying) with Response {

  protected def createInstance(message: this.type): this.type =
    new ResponseImpl(message.underlying).asInstanceOf[this.type]

}
