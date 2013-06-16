package org.sisioh.trinity.application

import org.sisioh.trinity.domain._

trait TrinityApplication {

  val config: Config

  val routeRepository: RouteRepositoryOnMemory

  def addRoute(route: Route):Unit = {
    routeRepository.store(route)
  }

  def getRoute(routeId: RouteId) = routeRepository.resolve(routeId)
}

object TrinityApplication {

  def apply(config: Config, globalSetting: Option[GlobalSetting] = None) =
    new TrinityApplicationImpl(config, globalSetting)

}