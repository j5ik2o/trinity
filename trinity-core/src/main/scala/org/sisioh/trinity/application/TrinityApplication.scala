package org.sisioh.trinity.application

import org.sisioh.trinity.domain._
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.domain.RouteId

trait TrinityApplication {

  val config: Config

  val routeRepository: RouteRepositoryOnMemory

  def addRoute(method: HttpMethod, path: String, action: Action)(implicit pathPatternParser: PathPatternParser) {
    addRoute(Route(method, path, action))
  }

  def addRoute(route: Route): Unit = {
    routeRepository.store(route)
  }

  def getRoute(routeId: RouteId) = routeRepository.resolve(routeId)
}

object TrinityApplication {

  def apply(config: Config, globalSetting: Option[GlobalSetting] = None) =
    new TrinityApplicationImpl(config, globalSetting)

}