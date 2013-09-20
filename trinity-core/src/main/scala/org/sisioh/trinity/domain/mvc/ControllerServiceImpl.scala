package org.sisioh.trinity.domain.mvc

import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.TrinityException
import org.sisioh.trinity.domain.io.Service
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

case class ControllerServiceImpl
(routeRepository: RouteRepository,
 controllerRepository: ControllerRepository,
 globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None)
(implicit executor: ExecutionContext)
  extends Service[Request, Response] with LoggingEx {

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
    }
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

  /**
   * [[org.sisioh.trinity.domain.mvc.Route]]が保持する[[org.sisioh.trinity.domain.mvc.Action]]
   * に対してリクエストを実行する。
   *
   * @param request [[org.sisioh.trinity.domain.mvc.Request]]
   * @return [[org.sisioh.trinity.domain.mvc.Response]]
   */
  protected def dispatchRequest(request: Request): Future[Response] = {
    implicit val ctx = SyncEntityIOContext
    routeRepository.find {
      case Route(RouteId(m, pattern), controllerId, _) =>
        val hasController = controllerRepository.containsByIdentity(controllerId)
        val routeParamsOpt = pattern(request.path.split('?').head)
        if (hasController.toOption.get && routeParamsOpt.isDefined && m == request.method)
          true
        else
          false
    }.map {
      case Route(RouteId(_, pattern), _, action) =>
        val routeParamsOpt = pattern(request.path.split('?').head)
        val newRequest = routeParamsOpt.map {
          routeParams =>
            request.withRouteParams(request.routeParams ++ routeParams)
        }.getOrElse(request)
        action(newRequest)
    }.getOrElse {
      notFoundHandler(request)
    }
  }

  def apply(request: Request): Future[Response] = {
    Try {
      dispatchRequest(request).recoverWith {
        case throwable: Throwable =>
          warn("occurred error", throwable)
          errorHandler(request, throwable)
      }
    }.recoverWith {
      case throwable: Exception =>
        warn("occurred error", throwable)
        Try(errorHandler(request, throwable))
    }.getOrElse {
      error("occurred other error")
      Future.failed(TrinityException(Some("Other Exception")))
    }
  }

}
