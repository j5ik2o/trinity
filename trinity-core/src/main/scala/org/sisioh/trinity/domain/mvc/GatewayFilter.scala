package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Response => IOResponse}
import com.twitter.finagle.{Service, Filter}
import com.twitter.util.Future

case class GatewayFilter(actionOpt: Option[Action[Request, Response]] = None)
  extends Filter[IORequest, IOResponse, Request, Response] {

  def apply(ioRequest: IORequest, service: Service[Request, Response]): Future[IOResponse] = {
    val request = Request.fromUnderlying(ioRequest)
    service(request.withAction(actionOpt)).map {
      responseIn =>
        responseIn.underlying
    }
  }

}
