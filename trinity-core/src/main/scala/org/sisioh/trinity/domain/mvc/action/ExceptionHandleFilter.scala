package org.sisioh.trinity.domain.mvc.action

import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.{GlobalSettings, SimpleFilter}
import scala.concurrent.{ExecutionContext, Future}
import org.sisioh.scala.toolbox.LoggingEx

/**
 * 例外をハンドリングするためのフィルター。
 *
 * @param globalSettings
 * @param executor
 */
case class ExceptionHandleFilter
(globalSettings: Option[GlobalSettings[Request, Response]] = None)
(implicit executor: ExecutionContext)
  extends SimpleFilter[Request, Response] with LoggingEx {

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
      InternalServerErrorAction(newRequest)
    }
  }

  def apply(requestIn: Request, action: Action[Request, Response]): Future[Response] = {
    action(requestIn).recoverWith {
      case throwable =>
        error("caught error.", throwable)
        errorHandler(requestIn, throwable)
    }
  }

}
