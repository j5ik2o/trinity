package org.sisioh.trinity.domain.mvc.routing

import java.util.UUID
import java.util.concurrent.atomic.AtomicLong
import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import org.sisioh.trinity.domain.io.http.Method
import scala.concurrent.Future
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.controller.Controller
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{PathPatternParser, PathPattern}

/**
 * ルートを表すエンティティ。
 */
private[mvc]
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
  //val controllerId: Identity[UUID]

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
   * @param action [[org.sisioh.trinity.domain.mvc.action.Action]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](identity: RouteId, action: Action[Req, Rep]): Route[Req, Rep] =
    new RouteImpl(identity, action)

  /**
   * ファクトリメソッド。
   *
   * @param method メソッド
   * @param pathPattern [[org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPattern]]
   * @param action [[org.sisioh.trinity.domain.mvc.action.Action]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](method: Method.Value, pathPattern: PathPattern, action: Action[Req, Rep]): Route[Req, Rep] =
    new RouteImpl(RouteId(method, pathPattern), action)

  /**
   * ファクトリメソッド。
   *
   * @param method メソッド
   * @param path パス
   * @param action [[org.sisioh.trinity.domain.mvc.action.Action]]
   * @param pathPatternParser [[org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPatternParser]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](method: Method.Value, path: String, action: Action[Req, Rep])
                     (implicit pathPatternParser: PathPatternParser): Route[Req, Rep] = {
    val pathPattern = pathPatternParser(path)
    new RouteImpl(RouteId(method, pathPattern), action)
  }

  /**
   * エクストラクタメソッド。
   *
   * @param route [[org.sisioh.trinity.domain.mvc.routing.Route]]
   * @return 構成要素
   */
  def unapply[Req, Rep](route: Route[Req, Rep]): Option[(RouteId, Action[Req, Rep])] =
    Some(route.identity, route.action)

  private val orderGenerator = new AtomicLong()

}

private[domain]
class RouteImpl[Req, Rep]
(val identity: RouteId, val action: Action[Req, Rep]) extends Route[Req, Rep]