package org.sisioh.trinity.domain.mvc.http

import org.jboss.netty.handler.codec.http.multipart.{MixedFileUpload, HttpPostRequestDecoder}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer.toNetty
import org.sisioh.trinity.domain.io.http.{Request => IORequest, AbstractRequestProxy, Methods, ProtocolVersion}
import org.sisioh.trinity.domain.mvc.GlobalSettings
import org.sisioh.trinity.domain.mvc.action.Action
import scala.collection.JavaConverters._
import scala.util.Try
import org.sisioh.trinity.domain.io.http

private[http]
class RequestImpl
(override val underlying: IORequest,
 val action: Option[Action[Request, Response]],
 val routeParams: Map[String, String],
 val globalSettings: Option[GlobalSettings[Request, Response]],
 val error: Option[Throwable])
  extends AbstractRequestProxy(underlying) with Request {

  def this(method: Methods.Value,
           uri: String,
           action: Option[Action[Request, Response]],
           routeParams: Map[String, String],
           globalSettings: Option[GlobalSettings[Request, Response]],
           error: Option[Throwable],
           protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11) =
    this(IORequest(method, uri, protocolVersion), action, routeParams, globalSettings, error)

  val toUnderlyingAsFinagle = underlying.toUnderlyingAsFinagle

  protected def createInstance(message: this.type): this.type =
    new RequestImpl(
      message.underlying,
      message.action,
      message.routeParams,
      message.globalSettings,
      message.error
    ).asInstanceOf[this.type]


  def multiParams: Try[Map[String, MultiPartItem]] = Try {
    if (method == Methods.Post) {
      content.markReaderIndex()
      val httpPostRequestDecoder = new HttpPostRequestDecoder(toUnderlyingAsFinagle)
      val m = if (httpPostRequestDecoder.isMultipart) {
        httpPostRequestDecoder.getBodyHttpDatas.asScala.map {
          data =>
            data.getName -> MultiPartItem(data.asInstanceOf[MixedFileUpload])
        }.toMap
      } else Map.empty[String, MultiPartItem]
      content.resetReaderIndex()
      m
    } else Map.empty[String, MultiPartItem]
  }

  def response: http.Response =
    Response(underlying.response)

  def withAction(action: Option[Action[Request, Response]]): this.type =
    new RequestImpl(underlying, action, routeParams, globalSettings, error).asInstanceOf[this.type]

  def withRouteParams(routeParams: Map[String, String]): this.type =
    new RequestImpl(underlying, action, routeParams, globalSettings, error).asInstanceOf[this.type]

  def withError(error: Throwable): this.type =
    new RequestImpl(underlying, action, routeParams, globalSettings, Some(error)).asInstanceOf[this.type]

}
