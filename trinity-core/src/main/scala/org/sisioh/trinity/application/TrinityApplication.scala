package org.sisioh.trinity.application

import org.sisioh.trinity.domain._
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.domain.RouteId
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}

trait TrinityApplication extends Routes {

  val config: Config

  val statsReceiver: StatsReceiver

}

object TrinityApplication {

  def apply(config: Config, globalSetting: Option[GlobalSetting] = None) =
    new TrinityApplicationImpl(config, globalSetting)

}