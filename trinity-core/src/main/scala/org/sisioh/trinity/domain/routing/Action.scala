package org.sisioh.trinity.domain.routing

import com.twitter.finagle.http.Response
import com.twitter.util.{Future => TFuture, FuturePool}
import org.sisioh.trinity.infrastructure.FutureUtil._
import scala.concurrent.{Future => SFuture, ExecutionContext}
import org.sisioh.trinity.domain.http.TrinityRequest

/**
 * コントローラのアクションを表す値オブジェクト。
 */
trait Action extends (TrinityRequest => TFuture[Response])

/**
 * `com.twitter.util.Future`を返すアクションを生成するためのオブジェクト。
 */
object FutureAction {

  def apply(action: TrinityRequest => TFuture[Response]) = new Action {
    def apply(v1: TrinityRequest): TFuture[Response] = action(v1)
  }

}

/**
 * `com.twitter.finagle.http.Response`を返すアクションを生成するためのオブジェクト。
 */
object FuturePoolAction {

  def apply(action: TrinityRequest => Response)(implicit futurePool: FuturePool) = new Action {
    def apply(v1: TrinityRequest): TFuture[Response] = futurePool {
      action(v1)
    }
  }

}

/**
 * `scala.concurrent.Future`を返すアクションを生成するためのオブジェクト。
 */
object ScalaFutureAction {

  def apply(action: TrinityRequest => SFuture[Response])(implicit executor: ExecutionContext) = new Action {
    def apply(request: TrinityRequest): TFuture[Response] =
      action(request).toTwitter
  }

}
