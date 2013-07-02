package org.sisioh.trinity.infrastructure

import com.twitter.util.{Try => TTry}
import scala.util.{Try => STry}

/**
  * `scala.util.Try` と `com.twitter.util.Try` を相互に変換するためのユーティリティ。
  */
object TryUtil {

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

}
