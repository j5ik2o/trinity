package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.server.Server

/**
 * グローバル設定を表すトレイト。
 */
trait GlobalSettings[-Req <: Request, +Rep <: Response] {

  /**
   * リクエストに対するリソースが見つからなかった場合に呼ばれる`Action`。
   *
   * @return [[org.sisioh.trinity.domain.mvc.action.Action]]
   */
  def notFound: Option[Action[Req, Rep]] = None

  /**
   * エラーが発生した場合に呼ばれる`Action`。
   *
   * @return [[org.sisioh.trinity.domain.mvc.action.Action]]
   */
  def error: Option[Action[Req, Rep]] = None

  def onStart(server: Server): Unit

  def onStop(server: Server): Unit

}
