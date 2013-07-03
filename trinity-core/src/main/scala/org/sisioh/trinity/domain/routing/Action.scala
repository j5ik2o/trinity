package org.sisioh.trinity.domain.routing

import com.twitter.finagle.http.Response
import com.twitter.util.{Future => TFuture, FuturePool}
import org.sisioh.trinity.infrastructure.FutureUtil._
import scala.concurrent.{Future => SFuture, ExecutionContext}
import org.sisioh.trinity.domain.http.Request

/**
 * コントローラのアクションを表す値オブジェクト。
 */
trait Action extends (Request => TFuture[Response])

/**
 * `com.twitter.util.Future`を返すアクションを生成するためのオブジェクト。
 */
object FutureAction {

  def apply(action: Request => TFuture[Response]) = new Action {
    def apply(v1: Request): TFuture[Response] = action(v1)
  }

}

/**
 * `com.twitter.finagle.http.Response`を返すアクションを生成するためのオブジェクト。
 */
object FuturePoolAction {

  def apply(action: Request => Response)(implicit futurePool: FuturePool) = new Action {
    def apply(v1: Request): TFuture[Response] = futurePool {
      action(v1)
    }
  }

}

/**
 * `scala.concurrent.Future`を返すアクションを生成するためのオブジェクト。
 */
object ScalaFutureAction {

  def apply(action: Request => SFuture[Response])(implicit executor: ExecutionContext) = new Action {
    def apply(request: Request): TFuture[Response] =
      action(request).toTwitter
  }

}
