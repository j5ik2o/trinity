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

import com.twitter.util.{Try => TTry}
import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.{Try => STry}

/**
 * `scala.util.Try` と `com.twitter.util.Try` を相互に変換するためのユーティリティ。
 */
object TryConverters {

  /**
   * `com.twitter.util.Try` から `scala.util.Try` に変換するための暗黙的値クラス。
   *
   * @param twTry `com.twitter.util.Try`
   * @tparam T 値の型
   */
  implicit class TTryToSTry[T](val twTry: TTry[T]) extends AnyVal {

    /**
     * `scala.util.Try` を取得する。
     *
     * @return `scala.util.Try`
     */
    def toScala = STry(twTry.get)

  }

  /**
   * `scala.util.Try` から `com.twitter.util.Try` に変換するための暗黙的値クラス。
   *
   * @param sTry `scala.util.Try`
   * @tparam T 値の型
   */
  implicit class STryToTTry[T](val sTry: STry[T]) extends AnyVal {

    /**
     * [[com.twitter.util.Try]]を取得する。
     *
     * @return [[com.twitter.util.Try]]
     */
    def toTwitter = TTry(sTry.get)

  }


  /**
   * `scala.util.Try`を`scala.concurrent.Future`に変換する。
   *
   * @param sTry `scala.util.Try`
   * @tparam T 値の型
   * @return `scala.concurrent.Future`
   */
  implicit def tryToFuture[T](sTry: STry[T]) = new {
    def toFuture: Future[T] = sTry.map {
      e => Future.successful(e)
    }.recover {
      case ex => Future.failed(ex)
    }.get
  }

  implicit def tryFutureToFuture[T](sTry: STry[Future[T]]) = new {
    def toFuture: Future[T] = sTry.recover {
      case ex =>  Future.failed(ex)
    }.get
  }

}

