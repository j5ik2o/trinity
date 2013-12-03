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
package org.sisioh.trinity.domain.mvc.filter

import com.twitter.finagle.Service
import com.twitter.finagle.{Filter => FinagleFilter}
import com.twitter.util.{Future => TFuture}
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.util.FutureConverters._
import scala.concurrent.ExecutionContext
import scala.concurrent.{Future => SFuture}

/**
 * Trinity独自のフィルター。
 *
 * @tparam ReqIn
 * @tparam RepOut
 * @tparam ReqOut
 * @tparam RepIn
 */
trait Filter[-ReqIn, +RepOut, +ReqOut, -RepIn]
  extends ((ReqIn, Action[ReqOut, RepIn]) => SFuture[RepOut]) {

  def apply(requestIn: ReqIn, action: Action[ReqOut, RepIn]): SFuture[RepOut]

}

/**
 * コンパニオンオブジェクト。
 */
object Filter {

  /**
   * [[org.sisioh.trinity.domain.mvc.filter.Filter]]を`Finagle`の`Filter`に変換する。
   *
   * @param self
   * @param executor
   * @tparam ReqIn
   * @tparam RepOut
   * @tparam ReqOut
   * @tparam RepIn
   * @return
   */
  def toFinagleFilter[ReqIn, RepOut, ReqOut, RepIn](self: Filter[ReqIn, RepOut, ReqOut, RepIn])(implicit executor: ExecutionContext) =
    new FinagleFilter[ReqIn, RepOut, ReqOut, RepIn] {
      def apply(request: ReqIn, service: Service[ReqOut, RepIn]): TFuture[RepOut] = {
        self.apply(request, new Action[ReqOut, RepIn] {
          def apply(request: ReqOut) = service(request).toScala
        }).toTwitter
      }
    }

}
