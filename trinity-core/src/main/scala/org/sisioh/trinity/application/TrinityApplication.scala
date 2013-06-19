package org.sisioh.trinity.application

import org.sisioh.trinity.domain._
import org.jboss.netty.handler.codec.http.HttpMethod
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
import org.sisioh.trinity.domain.routing.{Routes, RouteId}
import org.sisioh.trinity.domain.controller.GlobalSetting
import org.sisioh.trinity.domain.config.Config

trait TrinityApplication extends Routes {

  val config: Config

  val statsReceiver: StatsReceiver

}

object TrinityApplication {

  def apply(config: Config, globalSetting: Option[GlobalSetting] = None) =
    new TrinityApplicationImpl(config, globalSetting)

}