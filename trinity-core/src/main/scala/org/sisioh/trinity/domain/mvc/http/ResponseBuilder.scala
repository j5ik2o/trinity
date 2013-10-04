package org.sisioh.trinity.domain.mvc.http

import org.sisioh.dddbase.core.lifecycle.ValueObjectBuilder
import org.sisioh.trinity.domain.io.buffer.{ChannelBuffers, ChannelBuffer}
import org.sisioh.trinity.domain.io.transport.codec.http.{CharsetUtil, Cookie, ResponseStatus, Version}
import scala.collection.mutable.Map

class ResponseBuilder extends ValueObjectBuilder[Response, ResponseBuilder] {

  private var version: Version.Value = _

  private var status: ResponseStatus.Value = _

  private val headers = Map.empty[String, Any]

  private var cookies: Seq[Cookie] = _

  private var content: ChannelBuffer = _


  def withVersion(version: Version.Value) = {
    addConfigurator(_.version = version)
    getThis
  }

  def withStatus(status: ResponseStatus.Value) = {
    addConfigurator(_.status = status)
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

  def withHeader(name: String, value: Any) = {
    addConfigurator {
      _.headers += (name -> value)
    }
    getThis
  }

  def withHeaders(headers: Seq[(String, Any)]) = {
    addConfigurator {
      builder =>
        builder.headers.clear()
        builder.headers ++= headers
    }
    getThis
  }

  def withoutHeader(name: String) = {
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
    Response(version, status).withHeaders(headers.toSeq).withCookies(cookies).withContent(content)
  }

  protected def apply(vo: Response, builder: ResponseBuilder): Unit = {
    builder.withVersion(vo.protocolVersion)
    builder.withStatus(vo.status)
    builder.withHeaders(vo.headers)
    builder.withCookies(vo.cookies)
    builder.withContent(vo.content)
  }

}
