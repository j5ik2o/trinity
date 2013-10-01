package org.sisioh.trinity.domain.mvc

import org.sisioh.scala.toolbox.LoggingEx
import com.twitter.finagle.Service
import com.twitter.util.Future
import org.sisioh.trinity.infrastructure.util.FutureConverters._
import scala.concurrent.ExecutionContext
import scala.util.Try
import org.sisioh.trinity.domain.TrinityException

case class ActionExecuteService
(globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None)
(implicit executor: ExecutionContext)
  extends Service[Request, Response] with LoggingEx {

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

  /**
   * アクションが見つからない場合のリカバリを行うためのハンドラ。
   *
   * @return `Future`にラップされた[[com.twitter.finagle.http.Request]]
   */
  protected def notFoundHandler: Action[Request, Response] = {
    globalSettingsOpt.flatMap {
      _.notFound
    }.getOrElse(NotFoundHandleAction)
  }

  def apply(request: Request): Future[Response] = {
    Try {
      request.execute(notFoundHandler).toTwitter
    }.recover {
      case throwable: Exception =>
        warn("occurred error", throwable)
        errorHandler(request, throwable)
    }.getOrElse {
      error("occurred other error")
      Future.exception(TrinityException(Some("Other Exception")))
    }
  }

}
