package org.sisioh.trinity.domain.io.infrastructure.http

import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.{HttpRequest => NettyRequest}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.Cookie
import org.sisioh.trinity.domain.io.http.Method
import org.sisioh.trinity.domain.io.http.Method.toNetty
import org.sisioh.trinity.domain.io.http.Method.toTrintiy
import org.sisioh.trinity.domain.io.http.Request
import org.sisioh.trinity.domain.io.http.Version
import org.sisioh.trinity.domain.io.http.Version.toNetty


private[trinity]
class RequestImpl(override val toUnderlyingAsFinagle: FinagleRequest)
  extends AbstractMessage(toUnderlyingAsFinagle) with Request {

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
    new RequestImpl(message.toUnderlyingAsFinagle.asInstanceOf[FinagleRequest]).asInstanceOf[this.type]

  protected def mutateAsRequest(f: (NettyRequest) => Unit): this.type = {
    val cloned = if (!isMutable) {
      createInstance(this)
    } else {
      this
    }
    f(cloned.toUnderlyingAsFinagle)
    cloned
  }

  val method: Method.Value = toUnderlyingAsFinagle.getMethod()

  def withMethod(method: Method.Value): this.type = mutateAsRequest {
    _.setMethod(method)
  }

  val uri = toUnderlyingAsFinagle.getUri()

  def withUri(uri: String): this.type = mutateAsRequest {
    _.setUri(uri)
  }

}
