package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import org.sisioh.trinity.domain.io.transport.codec.http.{Request, RequestProxy}

case class AbstractRequestProxy(underlying: Request) extends RequestProxy {

  protected def createInstance(message: this.type): this.type =
    new AbstractRequestProxy(message.underlying).asInstanceOf[this.type]

}
