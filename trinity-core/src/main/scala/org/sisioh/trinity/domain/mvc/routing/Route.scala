package org.sisioh.trinity.domain.mvc.routing

import java.util.UUID
import java.util.concurrent.atomic.AtomicLong
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import org.sisioh.trinity.domain.io.transport.codec.http.Method
import scala.concurrent.Future
import org.sisioh.trinity.domain.mvc.{Controller, Action}

/**
 * ルートを表すエンティティ。
 */
trait Route[Req, Rep]
  extends Entity[RouteId]
  with EntityCloneable[RouteId, Route[Req, Rep]]
  with Ordered[Route[Req, Rep]] {

  def compare(that: Route[Req, Rep]): Int = {
    this.order compareTo that.order
  }

  private val order = Route.orderGenerator.getAndIncrement

  /**
   * コントローラID
   */
  val controllerId: Identity[UUID]

  /**
   * アクション
   */
  val action: Action[Req, Rep]

  /**
   * アクションを実行する。
   *
   * @param request リクエスト
   * @return `Future`
   */
  def apply(request: Req): Future[Rep] = action(request)

}

/**
 * コンパニオンオブジェクト。
 */
object Route {

  /**
   * ファクトリメソッド。
   *
   * @param identity [[org.sisioh.trinity.domain.mvc.routing.RouteId]]
   * @param controllerId コントローラID
   * @param action [[org.sisioh.trinity.domain.mvc.Action]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](identity: RouteId, controllerId: Identity[UUID], action: Action[Req, Rep]): Route[Req, Rep] =
    new RouteImpl(identity, controllerId, action)

  /**
   * ファクトリメソッド。
   *
   * @param identity [[org.sisioh.trinity.domain.mvc.routing.RouteId]]
   * @param controller コントローラ
   * @param action [[org.sisioh.trinity.domain.mvc.Action]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](identity: RouteId, controller: Controller, action: Action[Req, Rep]): Route[Req, Rep] =
    new RouteImpl(identity, controller.identity, action)

  /**
   * ファクトリメソッド。
   *
   * @param method メソッド
   * @param pathPattern [[org.sisioh.trinity.domain.mvc.routing.PathPattern]]
   * @param controller [[org.sisioh.trinity.domain.mvc.Controller]]
   * @param action [[org.sisioh.trinity.domain.mvc.Action]]
   * @return [[org.sisioh.trinity.domain.mvc.Action]]
   */
  def apply[Req, Rep](method: Method.Value, pathPattern: PathPattern, controller: Controller, action: Action[Req, Rep]): Route[Req, Rep] =
    apply(method, pathPattern, controller.identity, action)

  /**
   * ファクトリメソッド。
   *
   * @param method メソッド
   * @param pathPattern [[org.sisioh.trinity.domain.mvc.routing.PathPattern]]
   * @param controllerId コントローラID
   * @param action [[org.sisioh.trinity.domain.mvc.Action]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](method: Method.Value, pathPattern: PathPattern, controllerId: Identity[UUID], action: Action[Req, Rep]): Route[Req, Rep] =
    apply(RouteId(method, pathPattern), controllerId, action)

  /**
   * ファクトリメソッド。
   *
   * @param method メソッド
   * @param path パス
   * @param controllerId コントローラID
   * @param action [[org.sisioh.trinity.domain.mvc.Action]]
   * @param pathPatternParser [[org.sisioh.trinity.domain.mvc.routing.PathPatternParser]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](method: Method.Value, path: String, controllerId: Identity[UUID], action: Action[Req, Rep])
                     (implicit pathPatternParser: PathPatternParser): Route[Req, Rep] = {
    val pathPattern = pathPatternParser(path)
    apply(RouteId(method, pathPattern), controllerId, action)
  }

  /**
   * ファクトリメソッド。
   *
   * @param method メソッド
   * @param path パス
   * @param controller コントローラ
   * @param action [[org.sisioh.trinity.domain.mvc.Action]]
   * @param pathPatternParser [[org.sisioh.trinity.domain.mvc.routing.PathPatternParser]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](method: Method.Value, path: String, controller: Controller, action: Action[Req, Rep])
                     (implicit pathPatternParser: PathPatternParser): Route[Req, Rep] =
    apply[Req, Rep](method, path, controller.identity, action)

  /**
   * エクストラクタメソッド。
   *
   * @param route [[org.sisioh.trinity.domain.mvc.routing.Route]]
   * @return 構成要素
   */
  def unapply[Req, Rep](route: Route[Req, Rep]): Option[(RouteId, Identity[UUID], Action[Req, Rep])] =
    Some(route.identity, route.controllerId, route.action)

  private val orderGenerator = new AtomicLong()

}

private[domain]
class RouteImpl[Req, Rep]
(val identity: RouteId, val controllerId: Identity[UUID], val action: Action[Req, Rep]) extends Route[Req, Rep]