package org.sisioh.trinity.application

import com.twitter.finagle.SimpleFilter
import com.twitter.finagle.http.{Response, Request}
import com.twitter.finagle.stats.StatsReceiver
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.controller.{Controller, GlobalSettings}
import org.sisioh.trinity.domain.routing.Routes
import com.twitter.finagle.tracing.{NullTracer, Tracer}
import com.twitter.ostrich.admin.RuntimeEnvironment

/**
 * `Trinity`のアプリケーション本体。
 */
trait TrinityApplication extends Routes {

  /**
   * [[org.sisioh.trinity.domain.config.Config]]
   */
  val config: Config

  /**
   * [[com.twitter.finagle.stats.StatsReceiver]]
   */
  val statsReceiver: StatsReceiver

  /**
   * コントローラを登録する。
   *
   * @param controller コントローラ
   */
  def registerController(controller: Controller): Unit

  /**
   * コントローラの列を登録する。
   *
   * @param controllers コントローラの列
   */
  def registerControllers(controllers: Seq[Controller]): Unit =
    controllers.foreach(registerController)

  /**
   * フィルターを登録する。
   *
   * @param filter フィルター
   */
  def registerFilter(filter: SimpleFilter[Request, Response]): Unit

  /**
   * フィルターの列を登録する。
   *
   * @param filters フィルターの列
   */
  def registerFilters(filters: Seq[SimpleFilter[Request, Response]]): Unit =
    filters.foreach(registerFilter)

  /**
   * アプリケーションを開始する。
   */
  def start(): Unit

  /**
   * アプリケーションを開始する。
   *
   * @param tracer トレーサ
   * @param runtimeEnv 実行環境
   */
  def start
  (tracer: Tracer = NullTracer,
   runtimeEnv: RuntimeEnvironment = new RuntimeEnvironment(this)): Unit

  /**
   * アプリケーションを終了する。
   */
  def shutdown(): Unit

}

/**
 * コンパニオンオブジェクト。
 */
object TrinityApplication {

  /**
   * ファクトリメソッド。
   *
   * @param config [[org.sisioh.trinity.domain.config.Config]]
   * @param globalSetting [[org.sisioh.trinity.domain.controller.GlobalSettings]]
   * @return [[org.sisioh.trinity.application.TrinityApplication]]
   */
  def apply(config: Config, globalSetting: Option[GlobalSettings] = None): TrinityApplication = synchronized {
    new TrinityApplicationImpl(config, globalSetting)
  }


}