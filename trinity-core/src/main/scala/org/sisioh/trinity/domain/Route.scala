package org.sisioh.trinity.domain

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.dddbase.core.{EntityCloneable, Entity, Identity}

case class RouteId(method: HttpMethod, pathPattern: PathPattern) extends Identity[(HttpMethod, PathPattern)] {
  def value: (HttpMethod, PathPattern) = (method, pathPattern)
}

trait Route extends Entity[RouteId] with EntityCloneable[RouteId, Route] {
  val action: Action

  def apply(request: Request): Future[Response]
}


object Route {

  def apply(identity: RouteId, action: Action): Route = new RouteImpl(identity, action)

  def apply(method: HttpMethod, path: String, action: Action)(implicit pathPatternParser: PathPatternParser): Route = {
    val regex = pathPatternParser(path)
    apply(RouteId(method, regex), action)
  }

  def unapply(route: Route): Option[(RouteId, Action)] = Some(route.identity, route.action)
}

private[domain]
class RouteImpl(val identity: RouteId, val action: Action) extends Route {
  def apply(request: Request): Future[Response] = action(request)
}
