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
package org.sisioh.trinity.util

import TryConversation._
import com.twitter.util.{Future => TFuture, Promise => TPromise, Try => TTry}
import scala.concurrent.{Future => SFuture, promise => SPromise, ExecutionContext}
import scala.language.implicitConversions
import scala.util.{Try => STry}

object FutureConversation {

  /**
   * `com.twitter.util.Future` を `scala.concurrent.Future` に変換するための暗黙的値クラス。
   *
   * @param future [[com.twitter.util.Future]]
   * @tparam T 値の型
   */
  implicit def TFutureToSFuture[T](future: TFuture[T]): SFuture[T] = {
    val prom = SPromise[T]()
    future.respond {
      t: TTry[T] =>
        prom.complete(t)
    }
    prom.future
  }

  /**
   * `scala.concurrent.Future` から `com.twitter.util.Future` に変換するための暗黙的値クラス。
   *
   * @param future `scala.concurrent.Future`
   * @tparam T 値の型
   */
  implicit def SFutureToTFuture[T](future: SFuture[T])(implicit executor: ExecutionContext): TFuture[T] = {
    val prom = TPromise[T]()
    future.onComplete {
      t: STry[T] =>
        prom.update(t)
    }
    prom
  }

}
