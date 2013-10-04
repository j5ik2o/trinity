package org.sisioh.trinity.domain.mvc.stats

import com.twitter.ostrich.stats.{Stats => SStats}
import org.sisioh.trinity.infrastructure.util.FutureConversation._
import scala.concurrent.{ExecutionContext, Future}

object Stats {

  def timeFutureNanos[A](name: String)(f: => Future[A])
                        (implicit executor: ExecutionContext): Future[A] =
    SStats.timeFutureNanos(name)(f)

  def timeFutureMicro[A](name: String)(f: => Future[A])
                        (implicit executor: ExecutionContext): Future[A] =
    SStats.timeFutureMicros(name)(f)

  def timeFutureMilliso[A](name: String)(f: => Future[A])
                          (implicit executor: ExecutionContext): Future[A] =
    SStats.timeFutureMillis(name)(f)

}