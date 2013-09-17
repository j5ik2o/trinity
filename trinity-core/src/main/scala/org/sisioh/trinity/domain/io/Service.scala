package org.sisioh.trinity.domain.io

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.language.implicitConversions

import org.sisioh.trinity.infrastructure.util.FutureConversation.SFutureToTFuture
import org.sisioh.trinity.infrastructure.util.FutureConversation.TFutureToSFuture

import com.twitter.finagle.{Service => FinagleService}

trait Service[-Req, +Rep] extends (Req => Future[Rep]) {

  def apply(request: Req): Future[Rep]

}

object Service {

  implicit def toTrinity[Req, Rep](underlying: FinagleService[Req, Rep])(implicit executor: ExecutionContext): Service[Req, Rep] =
    new Service[Req, Rep] {
      def apply(request: Req): Future[Rep] = underlying(request)
    }

  implicit def toFinagle[Req, Rep](self: Service[Req, Rep])(implicit executor: ExecutionContext): FinagleService[Req, Rep] =
    new FinagleService[Req, Rep] {
      def apply(request: Req) = self(request)
    }

}
