package org.sisioh.trinity.domain

import com.twitter.finagle.http.Response
import com.twitter.util.{Future => TFuture, FuturePool}
import org.sisioh.trinity.infrastructure.FutureUtil._
import scala.concurrent.{Future => SFuture, ExecutionContext}

trait Action extends (Request => TFuture[Response])

object FutureAction {

  def apply(action: Request => TFuture[Response]) = new Action {
    def apply(v1: Request): TFuture[Response] = action(v1)
  }

}

object SimpleAction {
  def apply(action: Request => Response)(implicit futurePool: FuturePool) = new Action {
    def apply(v1: Request): TFuture[Response] = futurePool {
      action(v1)
    }
  }
}

object ScalaFutureAction {

  def apply(action: Request => SFuture[Response])(implicit executor: ExecutionContext) = new Action {
    def apply(request: Request): TFuture[Response] =
      action(request).toTwitter
  }

}
