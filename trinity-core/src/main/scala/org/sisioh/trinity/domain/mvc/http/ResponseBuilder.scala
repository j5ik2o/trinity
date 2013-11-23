package org.sisioh.trinity.domain.mvc.http

import java.nio.charset.Charset
import org.json4s.JValue
import org.sisioh.dddbase.core.lifecycle.ValueObjectBuilder
import org.sisioh.trinity.domain.io.buffer.{ChannelBuffers, ChannelBuffer}
import org.sisioh.trinity.domain.io.http._
import scala.collection.mutable
import scala.concurrent.Future

case class ResponseBuilder() extends ValueObjectBuilder[Response, ResponseBuilder] {

  private var protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11

  private var responseStatus: ResponseStatus.Value = ResponseStatus.Ok

  private val headers = mutable.Map.empty[HeaderName, Any]

  private var cookies: Seq[Cookie] = Seq.empty

  private var content: ChannelBuffer = ChannelBuffer.empty

  def withJson(jValue: JValue) =
    withRenderer(JSON4SRenderer(jValue))

  def withRenderer(responseRender: ResponseRenderer) = {
    responseRender.render(this)
    getThis
  }

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

  def withContentType(contentType: ContentType) = {
    withHeader(HeaderNames.ContentType, contentType.toString)
  }

  def withContentType(contentType: String) = {
    withHeader(HeaderNames.ContentType, contentType)
  }

  private def toCopiedBuffer(value: String, charset: Charset = CharsetUtil.UTF_8) = {
    ChannelBuffers.copiedBuffer(value, charset)
  }

  def withTextHtml(html: String, charset: Charset = CharsetUtil.UTF_8) = {
    withContent(toCopiedBuffer(html, charset)).withHeader(HeaderNames.ContentType, "text/html")
  }

  def withTextPlain(text: String, charset: Charset = CharsetUtil.UTF_8) = {
    withContent(toCopiedBuffer(text, charset)).withHeader(HeaderNames.ContentType, "text/plain")
  }

  def withContent(body: => String, charset: Charset = CharsetUtil.UTF_8): ResponseBuilder =
    withContent(toCopiedBuffer(body, charset))


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

  def toFuture = Future.successful(build)

}
