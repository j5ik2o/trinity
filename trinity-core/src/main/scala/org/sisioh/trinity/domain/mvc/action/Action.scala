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

import scala.concurrent.Future

/**
 * アクションを表す値オブジェクト。
 *
 * @tparam Req リクエストの型
 * @tparam Rep レスポンスの型
 */
trait Action[-Req, +Rep] extends (Req => Future[Rep]) {

  def apply(request: Req): Future[Rep]

}

/**
 * コンパニオンオブジェクト。
 */
object Action {

  /**
   * ファクトリメソッド。
   *
   * @param f 関数
   * @tparam Req リクエストの型
   * @tparam Rep レスポンスの型
   * @return [[org.sisioh.trinity.domain.mvc.action.Action]]
   */
  def apply[Req, Rep](f: (Req) => Future[Rep]): Action[Req, Rep] = new Action[Req, Rep] {
    def apply(request: Req): Future[Rep] = f(request)
  }

}
