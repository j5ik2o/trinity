package org.sisioh.trinity.domain.routing

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import java.util.UUID
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.dddbase.core.Identity
import org.sisioh.trinity.domain.controller.ControllerRepository
import org.sisioh.trinity.domain.http.Request

trait Routes {

  val routeRepository: RouteRepositoryOnMemory

  val controllerRepository: ControllerRepository

  implicit def convert(action: Request => Future[Response]) = FutureAction(action)

  def addRoute(method: HttpMethod, path: String, controllerId: Identity[UUID], action: Action)(implicit pathPatternParser: PathPatternParser) {
    addRoute(Route(method, path, controllerId, action))
  }

  def addRoute(method: HttpMethod, path: String, controllerId: Identity[UUID])(action: Request => Future[Response])(implicit pathPatternParser: PathPatternParser) {
    addRoute(Route(method, path, controllerId, action))
  }

  def addRoute(route: Route): Unit = {
    routeRepository.store(route)
  }

  def getRoute(routeId: RouteId) = routeRepository.resolve(routeId)

}
