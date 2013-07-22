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

import org.sisioh.trinity.domain.routing.Action
import org.sisioh.trinity.domain.http.TrinityRequest
import com.twitter.util.Future
import com.twitter.finagle.http.Response

case object NotFoundUnHandleAction extends Action {

  def apply(request: TrinityRequest): Future[Response] = {
    Future.exception(ActionNotFoundException(Some(request.toString())))
  }

}
