package org.sisioh.trinity.domain.routing

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import java.util.UUID
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.dddbase.core.model.{Entity, EntityCloneable, Identity}
import org.sisioh.trinity.domain.http.Request

/**
 * [[org.sisioh.trinity.domain.routing.Route]]のための識別子。
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

  val controllerId: Identity[UUID]

  val action: Action

  /**
   *
   * @param request
   * @return
   */
  def apply(request: Request): Future[Response] = action(request)

}


object Route {

  def apply(identity: RouteId, controllerId: Identity[UUID], action: Action): Route = new RouteImpl(identity, controllerId, action)

  def apply(method: HttpMethod, path: String, controllerId: Identity[UUID], action: Action)(implicit pathPatternParser: PathPatternParser): Route = {
    val regex = pathPatternParser(path)
    apply(RouteId(method, regex), controllerId, action)
  }

  def unapply(route: Route): Option[(RouteId, Identity[UUID], Action)] = Some(route.identity, route.controllerId, route.action)
}

private[domain]
class RouteImpl(val identity: RouteId, val controllerId: Identity[UUID], val action: Action) extends Route

