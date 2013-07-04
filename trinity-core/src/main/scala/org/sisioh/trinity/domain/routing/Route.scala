package org.sisioh.trinity.domain.routing

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import java.util.UUID
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.dddbase.core.model.{Entity, EntityCloneable, Identity}
import org.sisioh.trinity.domain.http.TrinityRequest
import scala.language.implicitConversions
import org.sisioh.trinity.domain.controller.Controller

/**
 * [[org.sisioh.trinity.domain.routing.Route]]のための識別子。
 *
 * @param method
 * @param pathPattern
 */
case class RouteId(method: HttpMethod, pathPattern: PathPattern)
  extends Identity[(HttpMethod, PathPattern)] {
  def value: (HttpMethod, PathPattern) = (method, pathPattern)
}

/**
 * ルートを表すエンティティ。
 */
trait Route
  extends Entity[RouteId]
  with EntityCloneable[RouteId, Route]
  with Ordered[Route] {

  def compare(that: Route): Int = {
    val v1 = identity.value._1.compareTo(that.identity.value._1)
    if (v1 != 0) v1
    else {
      val v2 = identity.value._2.compareTo(that.identity.value._2)
      if (v2 != 0) v2
      else {
        0
      }
    }
  }

  /**
   * コントローラID
   */
  val controllerId: Identity[UUID]

  /**
   * アクション
   */
  val action: Action

  /**
   * アクションを実行する。
   *
   * @param request リクエスト
   * @return `com.twitter.util.Future`
   */
  def apply(request: TrinityRequest): Future[Response] = action(request)

}


/**
 * コンパニオンオブジェクト。
 */
object Route {

  def apply(identity: RouteId, controllerId: Identity[UUID], action: Action): Route =
    new RouteImpl(identity, controllerId, action)

  def apply(identity: RouteId, controller: Controller, action: Action): Route =
    new RouteImpl(identity, controller.identity, action)

  def apply(method: HttpMethod, path: String, controllerId: Identity[UUID], action: Action)
           (implicit pathPatternParser: PathPatternParser): Route = {
    val regex = pathPatternParser(path)
    apply(RouteId(method, regex), controllerId, action)
  }

  def apply(method: HttpMethod, path: String, controller: Controller, action: Action)
           (implicit pathPatternParser: PathPatternParser): Route =
    apply(method, path, controller.identity, action)

  def unapply(route: Route): Option[(RouteId, Identity[UUID], Action)] =
    Some(route.identity, route.controllerId, route.action)

}

private[domain]
class RouteImpl(val identity: RouteId, val controllerId: Identity[UUID], val action: Action) extends Route

