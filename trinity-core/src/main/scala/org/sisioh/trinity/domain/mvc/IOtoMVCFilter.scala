package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Response => IOResponse}
import org.sisioh.trinity.domain.io.{Filter => IOFilter, Service}
import scala.concurrent.{ExecutionContext, Future}

case class IOtoMVCFilter
(implicit exector: ExecutionContext)
  extends IOFilter[IORequest, IOResponse, Request, Response] {

  def apply(request: IORequest, service: Service[Request, Response]): Future[IOResponse] = {
    val requestOut = Request.fromUnderlying(request)
    service(requestOut).map {
      responseIn =>
        responseIn.underlying
    }
  }

}
