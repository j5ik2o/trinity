/*
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
package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.mvc._
import org.sisioh.trinity.domain.mvc.action.{NotFoundHandleAction, Action}
import org.sisioh.trinity.domain.mvc.filter.Filter
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{SinatraPathPatternParser, PathPatternParser}
import scala.concurrent.{Future, ExecutionContext}

/**
 * Represents the routing filter.
 *
 * @param routeRepository [[org.sisioh.trinity.domain.mvc.routing.RouteRepository]]
 * @param globalSettings [[org.sisioh.trinity.domain.mvc.GlobalSettings]]
 * @param executor [[org.sisioh.trinity.domain.mvc.GlobalSettings]]
 */
case class RoutingFilter
(routeRepository: RouteRepository,
 globalSettings: Option[GlobalSettings[Request, Response]])
(implicit executor: ExecutionContext)
  extends Filter[Request, Response, Request, Response] with LoggingEx {

  /**
   * Gets the handler to recovery request not found.
   *
   * @return wrapped [[Action]] around `scala.Option`
   */
  protected def notFoundHandler: Option[Action[Request, Response]] = {
    globalSettings.flatMap {
      _.notFound
    }.orElse(Some(NotFoundHandleAction))
  }

  /**
   * Gets the action with route-params from [[Request]].
   *
   * @param request [[Request]]
   * @return action with route-params
   */
  protected def getActionWithRouteParams(request: Request): Option[(Action[Request, Response], Map[String, String])] = {
    routeRepository.find {
      case Route(RouteId(m, pattern), _) =>
        val routeParamsOpt = pattern(request.path.split('?').head)
        if (routeParamsOpt.isDefined && m == request.method)
          true
        else
          false
    }.flatMap {
      case Route(RouteId(_, pattern), action) =>
        val routeParamsOpt = pattern(request.path.split('?').head)
        routeParamsOpt.map {
          routeParams =>
            (action, request.routeParams ++ routeParams)
        }
    }
  }

  def apply(request: Request, service: Action[Request, Response]): Future[Response] = {
    if (request.action.isDefined) {
      service(request)
    } else {
      val actionWithRouteParams = getActionWithRouteParams(request)
      val action = actionWithRouteParams.map(_._1).orElse(notFoundHandler)
      val routeParams = actionWithRouteParams.map(_._2).getOrElse(Map.empty)
      service(request.withAction(action).withRouteParams(routeParams))
    }
  }

}

/**
 * Represents the companion object for [[RoutingFilter]].
 */
object RoutingFilter extends LoggingEx {

  @deprecated("instead of createFromControllers", "1.0.1")
  def createForControllers(controllers: RouteDefHolder*)
                          (implicit executor: ExecutionContext,
                           globalSettings: Option[GlobalSettings[Request, Response]] = None,
                           pathPatternParser: PathPatternParser = SinatraPathPatternParser()): RoutingFilter =
    createFromControllers(controllers: _*)

  /**
   * Creates a instance from [[RouteDefHolder]] for controllers.
   *
   * @param controllers
   * @param executor
   * @param globalSettings
   * @param pathPatternParser
   * @return
   */
  def createFromControllers(controllers: RouteDefHolder*)
                          (implicit executor: ExecutionContext,
                           globalSettings: Option[GlobalSettings[Request, Response]] = None,
                           pathPatternParser: PathPatternParser = SinatraPathPatternParser()): RoutingFilter =
    withDebugScope("createForControllers") {
      createForActions {
        pathPatternParser =>
          val result = controllers.flatMap(_.getRouteDefs)
          debug(s"result = $result")
          result
      }
    }

  def createForActions(routeDefs: (PathPatternParser) => Seq[RouteDef])
                      (implicit executor: ExecutionContext,
                       globalSettings: Option[GlobalSettings[Request, Response]] = None,
                       pathPatternParser: PathPatternParser = SinatraPathPatternParser()): RoutingFilter = {
    val routeRepository = RouteRepository.ofMemory
    routeDefs(pathPatternParser).foreach {
      case RouteDef(method, pathPattern, action) =>
        routeRepository.store(Route(method, pathPattern, action))
    }
    RoutingFilter(routeRepository, globalSettings)
  }

}
