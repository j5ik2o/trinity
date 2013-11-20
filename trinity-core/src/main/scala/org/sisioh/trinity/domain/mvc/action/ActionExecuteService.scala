package org.sisioh.trinity.domain.mvc.action

import com.twitter.finagle.Service
import com.twitter.util.Future
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.TrinityException
import org.sisioh.trinity.domain.mvc._
import org.sisioh.trinity.infrastructure.util.FutureConverters._
import scala.Some
import scala.concurrent.ExecutionContext
import scala.util.Try
import org.sisioh.trinity.domain.mvc.http.{Response, Request}

case class ActionExecuteService
(globalSettings: Option[GlobalSettings[Request, Response]] = None)
(implicit executor: ExecutionContext)
  extends Service[Request, Response] with LoggingEx {

  /**
   * エラー発生時のリカバリを行うためのハンドラ。
   *
   * @return `Future`にラップされた[[com.twitter.finagle.http.Request]]
   */
  protected def errorHandler(request: Request, throwable: Throwable): Future[Response] = {
    val newRequest = request.withError(throwable)
    globalSettings.map {
      _.error.map(_(newRequest)).
        getOrElse(InternalServerErrorAction(newRequest))
    }.getOrElse {
      InternalServerErrorAction(request)
    }.toTwitter
  }

  /**
   * アクションが見つからない場合のリカバリを行うためのハンドラ。
   *
   * @return `Future`にラップされた[[com.twitter.finagle.http.Request]]
   */
  protected def notFoundHandler: Action[Request, Response] = {
    globalSettings.flatMap {
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
