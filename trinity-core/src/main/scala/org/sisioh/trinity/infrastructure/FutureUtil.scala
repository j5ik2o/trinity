package org.sisioh.trinity.infrastructure

import TryUtil._
import com.twitter.util.{Future => TFuture, Promise => TPromise, Try => TTry}
import scala.concurrent.ExecutionContext
import scala.concurrent.{Future => SFuture, promise => SPromise}
import scala.util.{Try => STry}

/**
 * `scala.concurrent.Future` と `com.twitter.util.Future` を相互に変換するためのユーティリティ。
 */
object FutureUtil {

  /**
   * `com.twitter.util.Future` を `scala.concurrent.Future` に変換するための暗黙的値クラス。
   *
   * @param future [[com.twitter.util.Future]]
   * @tparam T 値の型
   */
  implicit class TFutureToSFuture[T](val future: TFuture[T]) extends AnyVal {

    /**
     * `scala.concurrent.Future` を取得する。
     *
     * @return `scala.concurrent.Future`
     */
    def toScala: SFuture[T] = {
      val prom = SPromise[T]
      future.respond {
        t: TTry[T] =>
          prom.complete(t.toScala)
      }
      prom.future
    }

  }

  /**
   * `scala.concurrent.Future` から `com.twitter.util.Future` に変換するための暗黙的値クラス。
   *
   * @param future `scala.concurrent.Future`
   * @tparam T 値の型
   */
  implicit class SFutureToTFuture[T](val future: SFuture[T]) extends AnyVal {

    /**
     * `com.twitter.util.Future` を取得する。
     *
     * @param executor `scala.concurrent.ExecutionContext`
     * @return `com.twitter.util.Future`
     */
    def toTwitter(implicit executor: ExecutionContext): TFuture[T] = {
      val prom = TPromise[T]
      future.onComplete {
        t: STry[T] =>
          prom.update(t.toTwitter)
      }
      prom
    }

  }

}
