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
import scala.language.implicitConversions
import scala.util.{Try => STry}

object TryConversation {

  /**
   * `com.twitter.util.Try` から `scala.util.Try` に変換するための暗黙的値クラス。
   *
   * @param twTry `com.twitter.util.Try`
   * @tparam T 値の型
   */
  implicit def TTryToSTry[T](twTry: TTry[T]): STry[T] =  STry(twTry.get())

  /**
   * `scala.util.Try` から `com.twitter.util.Try` に変換するための暗黙的値クラス。
   *
   * @param sTry `scala.util.Try`
   * @tparam T 値の型
   */
  implicit def STryToTTry[T](sTry: STry[T]): TTry[T] = TTry(sTry.get)

}
