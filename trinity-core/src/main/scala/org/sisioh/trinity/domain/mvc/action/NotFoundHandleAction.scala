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

import java.lang.String
import org.apache.commons.io.IOUtils
import org.sisioh.trinity.domain.io.http.{ContentType, ResponseStatus}
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.util.ResourceUtil
import scala.concurrent.Future
import scala.util.Try

/**
 * NOT_FOUND(404)を返す[[org.sisioh.trinity.domain.mvc.action.Action]]。
 */
case object NotFoundHandleAction extends Action[Request, Response] {

  def apply(request: Request): Future[Response] = {
    (for {
      fh <- ResourceUtil.getResourceInputStream("/404.html")
      bytes <- Try(IOUtils.toByteArray(fh))
      result <- Try(fh.read(bytes))
    } yield {
      val html = new String(bytes)
      Future.successful(
        Response(
          ResponseStatus.NotFound
        ).withContentType(ContentType.TextHtml).withContentAsString(html)
      )
    }).get
  }

}
