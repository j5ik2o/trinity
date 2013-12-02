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
