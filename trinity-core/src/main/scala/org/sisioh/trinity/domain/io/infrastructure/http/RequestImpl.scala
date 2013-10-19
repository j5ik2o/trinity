package org.sisioh.trinity.domain.io.infrastructure.http

import org.jboss.netty.handler.codec.http.DefaultHttpRequest
import org.jboss.netty.handler.codec.http.{HttpRequest => NettyRequest}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.Cookie
import org.sisioh.trinity.domain.io.http.Method
import org.sisioh.trinity.domain.io.http.Method.toNetty
import org.sisioh.trinity.domain.io.http.Method.toTrintiy
import org.sisioh.trinity.domain.io.http.Request
import org.sisioh.trinity.domain.io.http.Version
import org.sisioh.trinity.domain.io.http.Version.toNetty

import com.twitter.finagle.http.{Request => FinagleRequest}

private[trinity]
case class RequestImpl(override val finagle: FinagleRequest)
  extends AbstractMessage(finagle) with Request {

  def isRequest: Boolean = true

  def this(method: Method.Value,
           uri: String,
           headers: Seq[(String, Any)] = Seq.empty,
           cookies: Seq[Cookie] = Seq.empty,
           content: ChannelBuffer = ChannelBuffer.empty,
           version: Version.Value = Version.Http11) = {
    this(FinagleRequest(version, method, uri))
    setHeaders(headers)
    setCookies(cookies)
    setContent(content)
  }

  protected def createInstance(message: AbstractMessage): this.type =
    new RequestImpl(message.finagle.asInstanceOf[FinagleRequest]).asInstanceOf[this.type]

  protected def mutateAsRequest(f: (NettyRequest) => Unit): this.type = {
    val cloned = createInstance(this)
    f(cloned.finagle)
    cloned
  }

  val method: Method.Value = finagle.getMethod

  def withMethod(method: Method.Value): this.type = mutateAsRequest {
    _.setMethod(method)
  }

  val uri: String = finagle.getUri

  def withUri(uri: String): this.type = mutateAsRequest {
    _.setUri(uri)
  }

}
