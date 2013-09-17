package org.sisioh.trinity.domain.io

import com.twitter.finagle.{Filter => FinagleFilter}
import com.twitter.finagle.{Service => FinagleService}
import com.twitter.util.{Future => FinagleFuture}
import org.sisioh.trinity.domain.io.Service.toFinagle
import org.sisioh.trinity.infrastructure.util.FutureConversation.SFutureToTFuture
import org.sisioh.trinity.infrastructure.util.FutureConversation.TFutureToSFuture
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.language.implicitConversions


trait Filter[-ReqIn, +RepOut, +ReqOut, -RepIn]
  extends ((ReqIn, Service[ReqOut, RepIn]) => Future[RepOut]) {

  def apply(request: ReqIn, service: Service[ReqOut, RepIn]): Future[RepOut]

}

object Filter {

  implicit def toTrinity[ReqIn, RepOut, ReqOut, RepIn](underlying: FinagleFilter[ReqIn, RepOut, ReqOut, RepIn])(implicit executor: ExecutionContext): Filter[ReqIn, RepOut, ReqOut, RepIn] =
    new Filter[ReqIn, RepOut, ReqOut, RepIn] {
      def apply(request: ReqIn, service: Service[ReqOut, RepIn]): Future[RepOut] =
        underlying(request, service)
    }

  implicit def toFinagle[ReqIn, RepOut, ReqOut, RepIn](self: Filter[ReqIn, RepOut, ReqOut, RepIn])(implicit executor: ExecutionContext) =
    new FinagleFilter[ReqIn, RepOut, ReqOut, RepIn] {
      def apply(request: ReqIn, service: FinagleService[ReqOut, RepIn]): FinagleFuture[RepOut] = {
        self(request, service)
      }
    }

}
