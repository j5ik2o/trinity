package org.sisioh.trinity.domain.routing

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.domain.controller.{Controller, ControllerRepository}
import org.sisioh.trinity.domain.http.TrinityRequest
import scala.language.implicitConversions

trait Routes {

  val routeRepository: RouteRepository

  val controllerRepository: ControllerRepository

  implicit def convert(action: TrinityRequest => Future[Response]) = FutureAction(action)

  def addRoute(method: HttpMethod, pathPattern: PathPattern, controller: Controller, action: Action): Unit = {
    addRoute(Route(method, pathPattern, controller, action))
  }

  def addRoute(method: HttpMethod, path: String, controller: Controller, action: Action)(implicit pathPatternParser: PathPatternParser) {
    addRoute(Route(method, path, controller.identity, action))
  }

  def addRoute(method: HttpMethod, path: String, controller: Controller)(action: TrinityRequest => Future[Response])(implicit pathPatternParser: PathPatternParser) {
    addRoute(Route(method, path, controller.identity, action))
  }

  def addRoute(route: Route): Unit = {
    routeRepository.store(route)
  }

  def addRoutes(routes: Seq[Route]): Unit =
    routes.foreach(addRoute)

  def getRoute(routeId: RouteId) = routeRepository.resolve(routeId)

}
