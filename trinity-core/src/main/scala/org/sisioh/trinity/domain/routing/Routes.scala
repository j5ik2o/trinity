/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
 * Copyright 2013 Sisioh Project and others. (http://sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
