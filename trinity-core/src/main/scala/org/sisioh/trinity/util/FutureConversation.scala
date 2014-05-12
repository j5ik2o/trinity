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

/**
 * Represents the implicit conversions to convert between `com.twitter.util.Future` and `scala.concurrent.Future`.
 */
object FutureConversation {

  /**
   * Gets a future as `scala.concurrent.Future`.
   *
   * @param future `com.twitter.util.Future`
   * @tparam T value's type
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
   * Gets a future as `com.twitter.util.Future`.
   *
   * @param future `scala.concurrent.Future`
   * @tparam T value's type
   */
  implicit def SFutureToTFuture[T](future: SFuture[T])
                                  (implicit executor: ExecutionContext): TFuture[T] = {
    val prom = TPromise[T]()
    future.onComplete {
      t: STry[T] =>
        prom.update(t)
    }
    prom
  }

}
