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

import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import scala.concurrent.Future

/**
 * シンプルな[[org.sisioh.trinity.domain.mvc.action.Action]]。
 */
trait SimpleAction extends Action[Request, Response]

/**
 * コンパニオンオブジェクト。
 */
object SimpleAction {

  def apply(f: (Request) => Future[Response]): SimpleAction = new SimpleAction {
    def apply(request: Request): Future[Response] = f(request)
  }

}
