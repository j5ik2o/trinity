package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.infrastructure.transport.codec.http.AbstractRequestProxy
import org.sisioh.trinity.domain.io.transport.codec.http
import org.sisioh.trinity.domain.io.transport.codec.http.Method

class RequestImpl
(underlying: http.Request,
 val routeParams: Map[String, String] = Map.empty,
 val errorOpt: Option[Throwable] = None)
  extends AbstractRequestProxy(underlying) with Request {

  val multiParams: Map[String, MultiPartItem] =
    if (method == Method.Post) {
      content.markReaderIndex()
      val m = MultiPartItem.apply(this)
      content.resetReaderIndex()
      m
    } else Map.empty[String, MultiPartItem]

  def path: String = underlying.path

  def withRouteParams(routeParams: Map[String, String]): this.type =
    new RequestImpl(underlying, routeParams).asInstanceOf[this.type]

  def error: Option[Throwable] = errorOpt

  def withError(error: Throwable): this.type =
    new RequestImpl(underlying, routeParams, Some(error)).asInstanceOf[this.type]

}
