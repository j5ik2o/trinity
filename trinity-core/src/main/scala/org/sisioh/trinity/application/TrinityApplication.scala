package org.sisioh.trinity.application

import com.twitter.finagle.SimpleFilter
import com.twitter.finagle.http.{Response, Request}
import com.twitter.finagle.stats.StatsReceiver
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.controller.{Controller, GlobalSettings}
import org.sisioh.trinity.domain.routing.Routes

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

  def registerController(controller: Controller): Unit

  def registerFilter(filter: SimpleFilter[Request, Response])

  def start(): Unit

  def shutdown(): Unit

}

/**
 * コンパニオンオブジェクト。
 */
object TrinityApplication {

  @volatile
  private var currentApplication: TrinityApplication = _

  /**
   * ファクトリメソッド。
   *
   * @param config [[org.sisioh.trinity.domain.config.Config]]
   * @param globalSetting [[org.sisioh.trinity.domain.controller.GlobalSettings]]
   * @return [[org.sisioh.trinity.application.TrinityApplication]]
   */
  def apply(config: Config, globalSetting: Option[GlobalSettings] = None): TrinityApplication = synchronized {
    if (currentApplication == null) {
      currentApplication = new TrinityApplicationImpl(config, globalSetting)
      currentApplication
    } else {
      currentApplication
    }
  }

  implicit def current = currentApplication

}