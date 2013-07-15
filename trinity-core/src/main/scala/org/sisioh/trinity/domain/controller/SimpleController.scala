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

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http._
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.http.TrinityRequest
import org.sisioh.trinity.domain.routing.{FutureAction, Route}

/**
 * シンプルなコントローラ。
 *
 * @param application [[org.sisioh.trinity.application.TrinityApplication]]
 */
abstract class SimpleController(implicit application: TrinityApplication)
  extends AbstractController with LoggingEx {

  // TODO PathPatternを渡せるようにする。

  protected def get(path: String)(callback: TrinityRequest => Future[Response]) {
    addRoute(HttpMethod.GET, path)(callback)
  }

  protected def delete(path: String)(callback: TrinityRequest => Future[Response]) {
    addRoute(HttpMethod.DELETE, path)(callback)
  }

  protected def post(path: String)(callback: TrinityRequest => Future[Response]) {
    addRoute(HttpMethod.POST, path)(callback)
  }

  protected def put(path: String)(callback: TrinityRequest => Future[Response]) {
    addRoute(HttpMethod.PUT, path)(callback)
  }

  protected def head(path: String)(callback: TrinityRequest => Future[Response]) {
    addRoute(HttpMethod.HEAD, path)(callback)
  }

  protected def patch(path: String)(callback: TrinityRequest => Future[Response]) {
    addRoute(HttpMethod.PATCH, path)(callback)
  }

  protected def addRoute(method: HttpMethod, path: String)(callback: TrinityRequest => Future[Response]) {
    routeRepository.store(
      Route(method, path, identity, FutureAction {
        request =>
          stats.timeFuture("%s/Root/%s".format(method.toString, path.stripPrefix("/"))) {
            callback(request)
          }
      }
      )
    )
  }

}
