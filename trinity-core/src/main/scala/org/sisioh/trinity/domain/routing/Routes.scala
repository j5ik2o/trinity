package org.sisioh.trinity.domain.routing

import org.jboss.netty.handler.codec.http.HttpMethod

trait Routes {

  val routeRepository: RouteRepositoryOnMemory

  def addRoute(method: HttpMethod, path: String, action: Action)(implicit pathPatternParser: PathPatternParser) {
    addRoute(Route(method, path, action))
  }

  def addRoute(route: Route): Unit = {
    routeRepository.store(route)
  }

  def getRoute(routeId: RouteId) = routeRepository.resolve(routeId)

}
