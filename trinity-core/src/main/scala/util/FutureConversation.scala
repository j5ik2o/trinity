package org.sisioh.trinity.infrastructure.util

import scala.language.implicitConversions
import TryConversation._
import com.twitter.util.{Future => TFuture, Promise => TPromise, Try => TTry}
import scala.concurrent.{Future => SFuture, promise => SPromise, ExecutionContext}
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
