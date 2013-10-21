package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.mvc._
import org.sisioh.trinity.domain.mvc.action.{NotFoundHandleAction, ErrorHandleAction, Action}
import org.sisioh.trinity.domain.mvc.controller.{ScalatraLikeController, ControllerRepository}
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{SinatraPathPatternParser, PathPatternParser}
import scala.concurrent.{Future, ExecutionContext}

/**
 * ルーティング用フィルター。
 *
 * @param routeRepository
 * @param globalSettingsOpt
 * @param executor
 */
case class RoutingFilter
(routeRepository: RouteRepository,
 globalSettingsOpt: Option[GlobalSettings[Request, Response]])
(implicit executor: ExecutionContext)
  extends Filter[Request, Response, Request, Response] with LoggingEx {

  /**
   * アクションが見つからない場合のリカバリを行うためのハンドラ。
   *
   * @return `Future`にラップされた[[com.twitter.finagle.http.Request]]
   */
  protected def notFoundHandler: Option[Action[Request, Response]] = {
    globalSettingsOpt.flatMap {
      _.notFound
    }.orElse(Some(NotFoundHandleAction))
  }

  /**
   * エラー発生時のリカバリを行うためのハンドラ。
   *
   * @return `Future`にラップされた[[com.twitter.finagle.http.Request]]
   */
  protected def errorHandler(request: Request, throwable: Throwable): Future[Response] = {
    val newRequest = request.withError(throwable)
    globalSettingsOpt.map {
      _.error.map(_(newRequest)).
        getOrElse(ErrorHandleAction(newRequest))
    }.getOrElse {
      ErrorHandleAction(request)
    }
  }

  protected def getActionWithRouteParams(request: Request): Option[(Action[Request, Response], Map[String, String])] = {
    implicit val ctx = SyncEntityIOContext
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
    if (request.actionOpt.isDefined) {
      service(request)
    } else {
      val actionWithRouteParams = getActionWithRouteParams(request)
      val action = actionWithRouteParams.map(_._1).orElse(notFoundHandler)
      val routeParams = actionWithRouteParams.map(_._2).getOrElse(Map.empty)
      service(request.withActionOpt(action).withRouteParams(routeParams))
    }
  }

}

object RoutingFilter {

  private implicit val ctx = SyncEntityIOContext

  def createForControllers(controllers: Seq[ScalatraLikeController])
                      (implicit executor: ExecutionContext,
                       globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None,
                       pathPatternParser: PathPatternParser = SinatraPathPatternParser()): RoutingFilter = {
    createForActions {
      pathPatternParser =>
        controllers.flatMap(_.toRouteDefs)
    }
  }

  def createForActions(routeDefs: (PathPatternParser) => Seq[RouteDef])
            (implicit executor: ExecutionContext,
             globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None,
             pathPatternParser: PathPatternParser = SinatraPathPatternParser()): RoutingFilter = {
    val routeRepository = RouteRepository.ofMemory
    routeDefs(pathPatternParser).foreach {
      case RouteDef(method, pathPattern, action) =>
        routeRepository.store(Route(method, pathPattern, action))
    }
    RoutingFilter(routeRepository, globalSettingsOpt)
  }

}
