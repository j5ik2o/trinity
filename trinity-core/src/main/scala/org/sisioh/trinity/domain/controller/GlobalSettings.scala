package org.sisioh.trinity.domain.controller

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.sisioh.trinity.domain.http.Request
import org.sisioh.trinity.application.TrinityApplication

/**
 * グローバル設定を表すトレイト。
 */
trait GlobalSettings {

  /**
   * リクエストに対するリソースが見つからなかった場合に呼ばれるハンドラ。
   *
   * @param request リクエスト
   * @return `Future[Response]`
   */
  def notFound(request: Request): Future[Response]

  /**
   * エラーが発生した場合に呼ばれるハンドラ。
   *
   * @param request リクエスト
   * @return `Future[Response]`
   */
  def error(request: Request): Future[Response]

  /**
   * アプリケーションが起動した際に呼ばれるハンドラ。
   *
   * @param application [[org.sisioh.trinity.application.TrinityApplication]]
   */
  def onStart(application: TrinityApplication) {}

  /**
   * アプリケーションが停止した際に呼ばれるハンドラ。
   *
   * @param application [[org.sisioh.trinity.application.TrinityApplication]]
   */
  def onStop(application: TrinityApplication) {}

}
