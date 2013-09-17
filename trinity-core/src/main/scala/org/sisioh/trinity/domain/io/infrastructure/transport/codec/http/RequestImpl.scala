package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import org.jboss.netty.handler.codec.http.{DefaultHttpRequest, HttpRequest => NettyRequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Request, Version}
import org.sisioh.trinity.domain.io.transport.codec.http.Method
import org.sisioh.trinity.domain.io.transport.codec.http.Method.{toNetty, toTrintiy}
import org.sisioh.trinity.domain.io.transport.codec.http.Version.toNetty

private[trinity] case class RequestImpl(override val underlying: NettyRequest)
  extends AbstractMessage(underlying) with Request {
	
  def this(httpVersion: Version.Value, method: Method.Value, uri: String) =
    this(new DefaultHttpRequest(httpVersion, method, uri)) 
  
  protected def createMessage(message: AbstractMessage): this.type =
    new RequestImpl(message.underlying.asInstanceOf[NettyRequest]).asInstanceOf[this.type]

  protected def mutateAsRequest(f: (NettyRequest) => Unit): this.type = {
    val cloned = createMessage(this)
    f(cloned.underlying)
    cloned
  }

  val method: Method.Value = underlying.getMethod

  def withMethod(method: Method.Value): this.type = mutateAsRequest {
    _.setMethod(method)
  }

  val uri: String = underlying.getUri

  def withUri(uri: String): this.type = mutateAsRequest {
    _.setUri(uri)
  }

}
