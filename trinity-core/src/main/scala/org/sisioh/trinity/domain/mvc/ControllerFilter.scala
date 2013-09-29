package org.sisioh.trinity.domain.mvc

import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.TrinityException
import scala.util.Try
import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Response => IOResponse}
import com.twitter.finagle.{Service, Filter}
import com.twitter.util.Future
import org.sisioh.trinity.infrastructure.util.FutureConverters._
import scala.concurrent.ExecutionContext

case class ControllerFilter
(routeRepository: RouteRepository,
 controllerRepository: ControllerRepository,
 globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None)
(implicit executor : ExecutionContext)
  extends Filter[IORequest, IOResponse, Request, Response] with LoggingEx {

  /**
   * アクションが見つからない場合のリカバリを行うためのハンドラ。
   *
   * @return `Future`にラップされた[[com.twitter.finagle.http.Request]]
   */
  protected def notFoundHandler(request: Request): Future[Response] = {
    globalSettingsOpt.map {
      _.notFound.map(_(request)).
        getOrElse(NotFoundHandleAction(request))
    }.getOrElse {
      NotFoundHandleAction(request)
    }.toTwitter
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

  def apply(request: IORequest, service: Service[Request, Response]): Future[IOResponse] = {
    val actionWithRouteParams = getActionWithRouteParams(Request.fromUnderlying(request))
    val requestOut = Request.fromUnderlying(
      request,
      actionWithRouteParams.map(_._1),
      actionWithRouteParams.map(_._2).getOrElse(Map.empty)
    )
    Try {
      service(requestOut).map {
        responseIn =>
          responseIn.underlying
      }
    }.recoverWith {
      case throwable: Exception =>
        warn("occurred error", throwable)
        Try(errorHandler(requestOut, throwable))
    }.getOrElse {
      error("occurred other error")
      Future.exception(TrinityException(Some("Other Exception")))
    }
  }


}
