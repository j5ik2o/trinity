package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.scala.toolbox.LoggingEx
import com.twitter.finagle.{Service, Filter}
import com.twitter.util.Future
import org.sisioh.trinity.infrastructure.util.FutureConverters._
import scala.concurrent.ExecutionContext
import org.sisioh.trinity.domain.mvc._
import org.sisioh.trinity.domain.mvc.Response
import org.sisioh.trinity.domain.mvc.Request
import scala.Some

/**
 * ルーティング用フィルター。
 *
 * @param routeRepository
 * @param controllerRepository
 * @param globalSettingsOpt
 * @param executor
 */
case class RoutingFilter
(routeRepository: RouteRepository,
 controllerRepository: ControllerRepository,
 globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None)
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
    }.toTwitter
  }

  protected def getActionWithRouteParams(request: Request): Option[(Action[Request, Response], Map[String, String])] = {
    implicit val ctx = SyncEntityIOContext
    routeRepository.find {
      case Route(RouteId(m, pattern), controllerId, _) =>
        val hasController = controllerRepository.containsByIdentity(controllerId)
        val routeParamsOpt = pattern(request.path.split('?').head)
        if (hasController.toOption.get && routeParamsOpt.isDefined && m == request.method)
          true
        else
          false
    }.flatMap {
      case Route(RouteId(_, pattern), _, action) =>
        val routeParamsOpt = pattern(request.path.split('?').head)
        routeParamsOpt.map {
          routeParams =>
            (action, request.routeParams ++ routeParams)
        }
    }
  }

  def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
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
