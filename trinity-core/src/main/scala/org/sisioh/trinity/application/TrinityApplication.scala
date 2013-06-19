package org.sisioh.trinity.application

import com.twitter.finagle.stats.StatsReceiver
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.controller.{Controller, GlobalSetting}
import org.sisioh.trinity.domain.routing.Routes

trait TrinityApplication extends Routes {

  val config: Config

  val statsReceiver: StatsReceiver

  def registerController(controller: Controller): Unit

  def start(): Unit

  def shutdown(): Unit

}

object TrinityApplication {

  def apply(config: Config, globalSetting: Option[GlobalSetting] = None): TrinityApplication =
    new TrinityApplicationImpl(config, globalSetting)

}