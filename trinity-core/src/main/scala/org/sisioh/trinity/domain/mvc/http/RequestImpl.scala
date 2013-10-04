package org.sisioh.trinity.domain.mvc.http

import org.sisioh.trinity.domain.io.infrastructure.transport.codec.http.AbstractRequestProxy
import org.sisioh.trinity.domain.io.transport.codec.http
import org.sisioh.trinity.domain.io.transport.codec.http.{Version, Method}
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.GlobalSettings

class RequestImpl
(underlying: http.Request,
 val action: Option[Action[Request, Response]],
 val routeParams: Map[String, String],
 val globalSettingsOpt: Option[GlobalSettings[Request, Response]],
 val errorOpt: Option[Throwable])
  extends AbstractRequestProxy(underlying) with Request {

  def this(method: Method.Value,
           uri: String,
           action: Option[Action[Request, Response]] = None,
           routeParams: Map[String, String] = Map.empty,
           globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None,
           errorOpt: Option[Throwable] = None,
           httpVersion: Version.Value = Version.Http11) =
    this(http.Request(httpVersion, method, uri), action, routeParams, globalSettingsOpt, errorOpt)

  val multiParams: Map[String, MultiPartItem] =
    if (method == Method.Post) {
      content.markReaderIndex()
      val m = MultiPartItem.apply(this)
      content.resetReaderIndex()
      m
    } else Map.empty[String, MultiPartItem]

  def path: String = underlying.path

  def withAction(action: Option[Action[Request, Response]]): this.type =
    new RequestImpl(underlying, action, routeParams, globalSettingsOpt, errorOpt).asInstanceOf[this.type]

  def withRouteParams(routeParams: Map[String, String]): this.type =
    new RequestImpl(underlying, action, routeParams, globalSettingsOpt, errorOpt).asInstanceOf[this.type]

  def withError(error: Throwable): this.type =
    new RequestImpl(underlying, action, routeParams, globalSettingsOpt, Some(error)).asInstanceOf[this.type]

}
