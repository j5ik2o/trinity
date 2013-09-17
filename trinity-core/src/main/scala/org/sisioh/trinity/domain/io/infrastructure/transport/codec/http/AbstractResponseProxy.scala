package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import org.sisioh.trinity.domain.io.transport.codec.http.{Response, ResponseProxy}

case class AbstractResponseProxy(underlying: Response) extends ResponseProxy {

  protected def createMessage(message: this.type): this.type =
    new AbstractResponseProxy(message.underlying).asInstanceOf[this.type]

}
