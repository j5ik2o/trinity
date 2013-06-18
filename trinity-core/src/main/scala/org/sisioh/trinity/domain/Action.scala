package org.sisioh.trinity.domain

import com.twitter.util.{FuturePool, Future => TFuture}
import com.twitter.finagle.http.Response
import scala.concurrent.{Future => SFuture, ExecutionContext}
import org.sisioh.trinity.infrastructure.FutureUtil._

trait Action extends (Request => TFuture[Response])

case class FutureAction(action: Request => TFuture[Response]) extends Action {
  def apply(request: Request): TFuture[Response] = action(request)
}

case class SimpleAction(action: Request => Response)(implicit futurePool: FuturePool) extends Action {
  def apply(request: Request): TFuture[Response] = futurePool {
    action(request)
  }
}

case class ScalaFutureAction(action: Request => SFuture[Response])(implicit executor: ExecutionContext) extends Action {

  def apply(request: Request): TFuture[Response] =
    action(request).toTwitter

}
