package org.sisioh.trinity.domain.mvc.http

import com.twitter.finagle.http.ParamMap
import com.twitter.finagle.http.{Request => FinagleRequest}
import java.net.InetSocketAddress
import org.jboss.netty.handler.codec.http.HttpRequest
import org.sisioh.trinity.domain.io.infrastructure.transport.codec.http.AbstractRequestProxy
import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Version, Method}
import org.sisioh.trinity.domain.mvc.GlobalSettings
import org.sisioh.trinity.domain.mvc.action.Action

private[http]
class RequestImpl
(override val underlying: IORequest,
 val actionOpt: Option[Action[Request, Response]],
 val routeParams: Map[String, String],
 val globalSettingsOpt: Option[GlobalSettings[Request, Response]],
 val errorOpt: Option[Throwable])
  extends AbstractRequestProxy(underlying) with Request {

  val netty: HttpRequest = underlying.netty

  protected def createInstance(message: this.type): this.type =
    new RequestImpl(
      message.underlying,
      message.actionOpt,
      message.routeParams,
      message.globalSettingsOpt,
      message.errorOpt
    ).asInstanceOf[this.type]

  def this(method: Method.Value,
           uri: String,
           actionOpt: Option[Action[Request, Response]] = None,
           routeParams: Map[String, String] = Map.empty,
           globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None,
           errorOpt: Option[Throwable] = None,
           httpVersion: Version.Value = Version.Http11) =
    this(IORequest(method, uri, httpVersion), actionOpt, routeParams, globalSettingsOpt, errorOpt)

  def params: ParamMap = finagle.params

  def containsParam(name: String) = finagle.containsParam(name)

  def encodeBytes: Array[Byte] = finagle.encodeBytes()

  def encodeString: String = finagle.encodeString()

  val multiParams: Map[String, MultiPartItem] =
    if (method == Method.Post) {
      content.markReaderIndex()
      val m = MultiPartItem.apply(this)
      content.resetReaderIndex()
      m
    } else Map.empty[String, MultiPartItem]

  def path: String = finagle.path

  def fileExtension = finagle.fileExtension


  def withAction(action: Option[Action[Request, Response]]): this.type =
    new RequestImpl(underlying, action, routeParams, globalSettingsOpt, errorOpt).asInstanceOf[this.type]

  def withRouteParams(routeParams: Map[String, String]): this.type =
    new RequestImpl(underlying, actionOpt, routeParams, globalSettingsOpt, errorOpt).asInstanceOf[this.type]

  def withError(error: Throwable): this.type =
    new RequestImpl(underlying, actionOpt, routeParams, globalSettingsOpt, Some(error)).asInstanceOf[this.type]

  def remoteSocketAddress: InetSocketAddress = finagle.remoteSocketAddress

}
