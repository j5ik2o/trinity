package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Response => IOResponse}
import org.sisioh.trinity.domain.io.{Filter => IOFilter, Service}
import scala.concurrent.{ExecutionContext, Future}

class MVCFilter[-ReqIn <: IORequest, +RepOut <: IOResponse, +ReqOut <: Request, RepIn <: Response](implicit exector: ExecutionContext)
  extends IOFilter[ReqIn, RepOut, ReqOut, RepIn] {

  def apply(request: ReqIn, service: Service[ReqOut, RepIn]): Future[RepOut] = {
    val requestOut = Request(request)
    service(requestOut.asInstanceOf[ReqOut]).map {
      responseIn =>
        Response(responseIn).asInstanceOf[RepOut]
    }
  }

}
