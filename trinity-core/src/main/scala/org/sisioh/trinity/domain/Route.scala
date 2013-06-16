package org.sisioh.trinity.domain

import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.dddbase.core.{EntityCloneable, Entity, Identity}
import org.sisioh.trinity.{ResponseBuilder, RequestAdaptor}

case class RouteId(method: HttpMethod, pathPattern: PathPattern) extends Identity[(HttpMethod, PathPattern)] {
  def value: (HttpMethod, PathPattern) = (method, pathPattern)
}

trait Route extends Entity[RouteId] with EntityCloneable[RouteId, Route] {
  val action: Action
  def apply(request: RequestAdaptor): Future[ResponseBuilder]
}

object Route {
  def apply(identity: RouteId, action: Action) = new RouteImpl(identity, action)
  def unapply(route: Route): Option[(RouteId, Action)] = Some(route.identity, route.action)
}

private[domain]
class RouteImpl(val identity: RouteId, val action: Action) extends Route {
  def apply(request: RequestAdaptor): Future[ResponseBuilder] = action(request)
}
