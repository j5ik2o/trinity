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
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.http.{TrinityRequest, ContentType}
import org.sisioh.trinity.domain.routing.{RouteRepositoryOnMemory, SinatraPathPatternParser, PathPatternParser}
import org.jboss.netty.handler.codec.http.HttpResponseStatus

abstract class AbstractController()(implicit val application: TrinityApplication)
  extends Controller {

  implicit protected val config: Config = application.config

  implicit protected val pathParser: PathPatternParser = new SinatraPathPatternParser()

  val routeRepository: RouteRepositoryOnMemory = new RouteRepositoryOnMemory

  val controllerRepository: ControllerRepository = new ControllerRepositoryOnMemory

  protected def redirect
  (location: String, message: String = "moved"): Future[Response] = {
    responseBuilder.withPlain(message).
      withStatus(HttpResponseStatus.MOVED_PERMANENTLY).
      withHeader("Location", location).toTrinityResponseFuture
  }

  protected def respondTo(r: TrinityRequest)(callback: PartialFunction[ContentType, Future[Response]]): Future[Response] = {
    if (!r.routeParams.get("format").isEmpty) {
      val format = r.routeParams("format")
      val mime = ContentType.getContentType("." + format)
      val contentType = ContentType.valueOf(mime).getOrElse(ContentType.All)
      if (callback.isDefinedAt(contentType)) {
        callback(contentType)
      } else {
        Future.exception(new RespondNotFoundException)
      }
    } else {
      r.accepts.find {
        mimeType =>
          callback.isDefinedAt(mimeType)
      }.map {
        contentType =>
          callback(contentType)
      }.getOrElse {
        Future.exception(new RespondNotFoundException)
      }
    }
  }

}
