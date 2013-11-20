package org.sisioh.trinity.domain.mvc.action

import scala.concurrent.Future

/**
 * アクションを表す値オブジェクト。
 *
 * @tparam Req リクエストの型
 * @tparam Rep レスポンスの型
 */
trait Action[-Req, +Rep] extends (Req => Future[Rep]) {

  def apply(request: Req): Future[Rep]

}

/**
 * コンパニオンオブジェクト。
 */
object Action {

  /**
   * ファクトリメソッド。
   *
   * @param f 関数
   * @tparam Req リクエストの型
   * @tparam Rep レスポンスの型
   * @return [[org.sisioh.trinity.domain.mvc.action.Action]]
   */
  def apply[Req, Rep](f: (Req) => Future[Rep]): Action[Req, Rep] = new Action[Req, Rep] {
    def apply(request: Req): Future[Rep] = f(request)
  }

}
