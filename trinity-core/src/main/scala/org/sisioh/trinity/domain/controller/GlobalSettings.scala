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

import com.twitter.finagle.http.{Response => FinagleResponse}
import com.twitter.util.Future
import org.sisioh.trinity.domain.http.{TrinityResponseImplicitSupport, TrinityRequest}
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.routing.Action

/**
 * グローバル設定を表すトレイト。
 */
trait GlobalSettings extends TrinityResponseImplicitSupport {

  /**
   * リクエストに対するリソースが見つからなかった場合に呼ばれる`Action`。
   *
   * @return [[org.sisioh.trinity.domain.routing.Action]]
   */
  def notFound: Option[Action] = None

  /**
   * エラーが発生した場合に呼ばれる`Action`。
   *
   * @return [[org.sisioh.trinity.domain.routing.Action]]
   */
  def error: Option[Action] = None

  /**
   * アプリケーションが起動した際に呼ばれるハンドラ。
   *
   * @param application [[org.sisioh.trinity.application.TrinityApplication]]
   */
  def onStart(application: TrinityApplication) {}

  /**
   * アプリケーションが停止した際に呼ばれるハンドラ。
   *
   * @param application [[org.sisioh.trinity.application.TrinityApplication]]
   */
  def onStop(application: TrinityApplication) {}

}
