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
package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.Request
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.server.Server

/**
 * グローバル設定を表すトレイト。
 */
trait GlobalSettings[-Req <: Request, +Rep <: Response] {

  /**
   * リクエストに対するリソースが見つからなかった場合に呼ばれる`Action`。
   *
   * @return [[org.sisioh.trinity.domain.mvc.action.Action]]
   */
  def notFound: Option[Action[Req, Rep]] = None

  /**
   * エラーが発生した場合に呼ばれる`Action`。
   *
   * @return [[org.sisioh.trinity.domain.mvc.action.Action]]
   */
  def error: Option[Action[Req, Rep]] = None

  def onStart(server: Server): Unit

  def onStop(server: Server): Unit

}
