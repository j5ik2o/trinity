package org.sisioh.trinity.domain.mvc

import com.twitter.finagle.Service
import com.twitter.finagle.{Filter => FinagleFilter}
import com.twitter.util.{Future => TFuture}
import org.sisioh.trinity.infrastructure.util.FutureConverters._
import scala.concurrent.{Future => SFuture, ExecutionContext}

trait Filter[-ReqIn, +RepOut, +ReqOut, -RepIn]
  extends ((ReqIn, Action[ReqOut, RepIn]) => SFuture[RepOut]) {

  def apply(requestIn: ReqIn, action: Action[ReqOut, RepIn]): SFuture[RepOut]

}

trait SimpleFilter[Req, Rep] extends Filter[Req, Rep, Req, Rep]

object Filter {


  def toFinagleFilter[ReqIn, RepOut, ReqOut, RepIn](self: Filter[ReqIn, RepOut, ReqOut, RepIn])(implicit executor: ExecutionContext) =
    new FinagleFilter[ReqIn, RepOut, ReqOut, RepIn] {
      def apply(request: ReqIn, service: Service[ReqOut, RepIn]): TFuture[RepOut] = {
        self.apply(request, new Action[ReqOut, RepIn] {
          def apply(request: ReqOut) = service(request).toScala
        }).toTwitter
      }
    }

}
