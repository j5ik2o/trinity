package org.sisioh.trinity.domain.controller

import com.twitter.finagle.http.{Response => FinagleResponse}
import com.twitter.util.Future
import org.sisioh.trinity.domain.http.{TrinityResponseImplicitSupport, TrinityRequest}
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.routing.Action

/**
 * グローバル設定を表すトレイト。
 */
trait GlobalSettings extends TrinityResponseImplicitSupport {

  /**
   * リクエストに対するリソースが見つからなかった場合に呼ばれる`Action`。
   *
   * @return [[org.sisioh.trinity.domain.routing.Action]]
   */
  def notFound: Option[Action] = None

  /**
   * エラーが発生した場合に呼ばれる`Action`。
   *
   * @return [[org.sisioh.trinity.domain.routing.Action]]
   */
  def error: Option[Action] = None

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
