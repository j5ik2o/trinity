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
package org.sisioh.trinity.domain.mvc.http

import org.sisioh.trinity.domain.io.http._
import scala.concurrent.Future

/**
 * Represents the trait to supports response processing.
 */
trait ResponseSupport {

  protected def responseBuilder = ResponseBuilder()

  protected def redirect(location: String, response: Option[Response] = None): Future[Response] = {
    val responseBuilder = ResponseBuilder().
      withResponseStatus(ResponseStatus.MovedPermanently).
      withHeader(HeaderNames.Location, location)
    val resp = response.fold(responseBuilder.build)(responseBuilder.build(_))
    Future.successful(resp)
  }

  protected def respondTo(request: Request)(callback: PartialFunction[ContentType, Future[Response]]): Future[Response] = {
    if (!request.routeParams.get("format").isEmpty) {
      val format = request.routeParams("format")
      val mime = MimeTypes.fileExtensionOf("." + format)
      val contentType = ContentTypes.valueOf(mime).getOrElse(ContentTypes.All)
      if (callback.isDefinedAt(contentType)) {
        callback(contentType)
      } else {
        Future.failed(new RespondNotFoundException)
      }
    } else {
      request.accepts.find {
        mimeType =>
          callback.isDefinedAt(mimeType)
      }.map {
        contentType =>
          callback(contentType)
      }.getOrElse {
        Future.failed(new RespondNotFoundException)
      }
    }
  }

}
