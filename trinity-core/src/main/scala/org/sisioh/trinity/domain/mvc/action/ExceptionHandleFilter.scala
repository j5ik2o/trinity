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
package org.sisioh.trinity.domain.mvc.action

import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.mvc.GlobalSettings
import org.sisioh.trinity.domain.mvc.filter.SimpleFilter
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import scala.concurrent.{ExecutionContext, Future}

/**
 * 例外をハンドリングするためのフィルター。
 *
 * @param globalSettings [[org.sisioh.trinity.domain.mvc.GlobalSettings]]
 * @param executor [[scala.concurrent.ExecutionContext]]
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
