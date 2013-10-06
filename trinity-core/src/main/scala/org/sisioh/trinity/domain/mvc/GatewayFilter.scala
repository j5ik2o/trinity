package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Response => IOResponse}
import com.twitter.finagle.{Service, Filter => FinagleFilter}
import com.twitter.util.Future
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import com.twitter.ostrich.stats.Stats

case class GatewayFilter(actionOpt: Option[Action[Request, Response]] = None)
  extends FinagleFilter[IORequest, IOResponse, Request, Response] {

  def apply(ioRequest: IORequest, service: Service[Request, Response]): Future[IOResponse] = {
    Stats.timeFutureNanos("GatewayFilter")({
      val request = Request.fromUnderlying(ioRequest)
      service(request.withAction(actionOpt)).map {
        responseIn =>
          responseIn.underlying
      }
    })
  }

}
