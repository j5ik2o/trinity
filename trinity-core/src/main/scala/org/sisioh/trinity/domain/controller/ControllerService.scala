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
package org.sisioh.trinity.domain.controller

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.util.{Return, Try, Future}
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.http.{TrinityResponseImplicitSupport, TrinityResponseBuilder, TrinityRequest}
import org.sisioh.trinity.domain.routing.{RouteId, Route}

/**
 * アクションにリクエストをディスパッチするためのFinagleサービス。
 *
 * @param application [[org.sisioh.trinity.application.TrinityApplication]]
 * @param globalSettingOpt [[org.sisioh.trinity.domain.controller.GlobalSettings]]
 */
class ControllerService(application: TrinityApplication, globalSettingOpt: Option[GlobalSettings] = None)
  extends Service[FinagleRequest, FinagleResponse] with LoggingEx with TrinityResponseImplicitSupport {

  /**
   * アクションが見つからない場合のリカバリを行うためのハンドラ。
   *
   * @return `Future`にラップされた[[com.twitter.finagle.http.Request]]
   */
  protected def notFoundHandler(request: TrinityRequest): Future[FinagleResponse] = {
    globalSettingOpt.map {
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
  protected def errorHandler(request: TrinityRequest, throwable: Throwable): Future[FinagleResponse] = {
    val newRequest = request.copy(error = Some(throwable))
    globalSettingOpt.map {
      _.error.map(_(newRequest)).
        getOrElse(ErrorHandleAction(newRequest))
    }.getOrElse {
      ErrorHandleAction(request)
    }
  }

  /**
   * [[org.sisioh.trinity.domain.http.TrinityResponseBuilder]]を生成して返す。
   *
   * @return [[org.sisioh.trinity.domain.http.TrinityResponseBuilder]]
   */
  protected def builder: TrinityResponseBuilder = TrinityResponseBuilder()

  /**
   * [[org.sisioh.trinity.domain.routing.Route]]が保持する[[org.sisioh.trinity.domain.routing.Action]]
   * に対してリクエストを実行する。
   *
   * @param request [[org.sisioh.trinity.domain.http.TrinityRequest]]
   * @return [[com.twitter.finagle.http.Response]]
   */
  protected def dispatchRequest(request: TrinityRequest): Future[FinagleResponse] =
    withDebugScope(s"dispatchRequest($request)") {
      application.routeRepository.find {
        case Route(RouteId(m, pattern), controllerId, _) =>
          val hasController = application.controllerRepository.contains(controllerId)
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
              request.copy(routeParams = request.routeParams ++ routeParams)
          }.getOrElse(request)
          scopedDebug("perform action")
          action(newRequest)
      }.getOrElse {
        scopedDebug("perform not found handler")
        notFoundHandler(request)
      }
    }

  def apply(rawRequest: FinagleRequest): Future[FinagleResponse] = {
    val adaptedRequest = TrinityRequest(rawRequest)
    Try {
      dispatchRequest(adaptedRequest).rescue {
        case throwable: Throwable =>
          warn("occurred error", throwable)
          errorHandler(adaptedRequest, throwable)
      }
    }.rescue {
      case throwable: Exception =>
        warn("occurred error" , throwable)
        Try(errorHandler(adaptedRequest, throwable))
    }.getOrElse {
      error("occurred other error")
      Future.exception(TrinityException(Some("Other Exception")))
    }
  }


}
