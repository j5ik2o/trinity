package org.sisioh.trinity.domain.mvc.http

import org.sisioh.dddbase.core.lifecycle.ValueObjectBuilder
import org.sisioh.trinity.domain.io.buffer.{ChannelBuffers, ChannelBuffer}
import org.sisioh.trinity.domain.io.http._
import scala.collection.mutable

case class ResponseBuilder() extends ValueObjectBuilder[Response, ResponseBuilder] {

  private var protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11

  private var responseStatus: ResponseStatus.Value = ResponseStatus.Ok

  private val headers = mutable.Map.empty[HeaderName, Any]

  private var cookies: Seq[Cookie] = Seq.empty

  private var content: ChannelBuffer = ChannelBuffer.empty


  def withProtocolVersion(protocolVersion: ProtocolVersion.Value) = {
    addConfigurator(_.protocolVersion = protocolVersion)
    getThis
  }

  def withResponseStatus(responseStatus: ResponseStatus.Value) = {
    addConfigurator(_.responseStatus = responseStatus)
    getThis
  }

  def withCookies(cookies: Seq[Cookie]) = {
    addConfigurator(_.cookies = cookies)
    getThis
  }

  def withContentAsString(body: => String): ResponseBuilder =
    withContent(ChannelBuffers.copiedBuffer(body, CharsetUtil.UTF_8))

  def withContent(body: => ChannelBuffer): ResponseBuilder = {
    addConfigurator(_.content = body)
    getThis
  }

  def withHeader(name: HeaderName, value: Any) = {
    addConfigurator {
      _.headers += (name -> value)
    }
    getThis
  }

  def withHeaders(headers: Seq[(HeaderName, Any)]) = {
    addConfigurator {
      builder =>
        builder.headers.clear()
        builder.headers ++= headers
    }
    getThis
  }

  def withoutHeader(name: HeaderName) = {
    addConfigurator {
      _.headers.remove(name)
    }

  }

  def withoutAllHeaders = {
    addConfigurator {
      _.headers.clear()
    }
    getThis
  }

  protected def getThis: ResponseBuilder = this

  protected def newInstance: ResponseBuilder = new ResponseBuilder()

  protected def createValueObject: Response = {
    Response(responseStatus, protocolVersion).withHeaders(headers.toSeq).withCookies(cookies).withContent(content)
  }

  protected def apply(vo: Response, builder: ResponseBuilder): Unit = {
    builder.withProtocolVersion(vo.protocolVersion)
    builder.withResponseStatus(vo.responseStatus)
    builder.withHeaders(vo.headers)
    builder.withCookies(vo.cookies)
    builder.withContent(vo.content)
  }

}
