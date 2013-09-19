package org.sisioh.trinity.domain.mvc

/**
 * グローバル設定を表すトレイト。
 */
trait GlobalSettings[-Req <: Request, +Rep <: Response] {

  /**
   * リクエストに対するリソースが見つからなかった場合に呼ばれる`Action`。
   *
   * @return [[org.sisioh.trinity.domain.mvc.Action]]
   */
  def notFound: Option[Action[Req, Rep]] = None

  /**
   * エラーが発生した場合に呼ばれる`Action`。
   *
   * @return [[org.sisioh.trinity.domain.mvc.Action]]
   */
  def error: Option[Action[Req, Rep]] = None

}
