package org.sisioh.trinity.application

import org.sisioh.trinity.domain.{RouteRepositoryOnMemory, GlobalSetting, Config, RouteRepository}

trait TrinityApplication {

  val config: Config

  val routeRepository: RouteRepositoryOnMemory

}

object TrinityApplication {

  def apply(config: Config, globalSetting: Option[GlobalSetting] = None) =
    new TrinityApplicationImpl(config, globalSetting)

}