package org.sisioh.trinity.domain.routing

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.dddbase.core.{EntityCloneable, Entity, Identity}
import org.sisioh.trinity.domain.{routing}
import org.sisioh.trinity.domain.http.Request

/**
 * [[routing.Route]]のための識別子。
 *
 * @param method
 * @param pathPattern
 */
case class RouteId(method: HttpMethod, pathPattern: PathPattern) extends Identity[(HttpMethod, PathPattern)] {
  def value: (HttpMethod, PathPattern) = (method, pathPattern)
}

/**
 * ルートを表すエンティティ。
 */
trait Route extends Entity[RouteId] with EntityCloneable[RouteId, Route] {

  val action: Action

  /**
   *
   * @param request
   * @return
   */
  def apply(request: Request): Future[Response] = action(request)

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
class RouteImpl(val identity: RouteId, val action: Action) extends Route

