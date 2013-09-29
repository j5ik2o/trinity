package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Response => IOResponse}
import com.twitter.finagle.{Service, Filter}
import com.twitter.util.Future

case class IOtoMVCFilter()
  extends Filter[IORequest, IOResponse, Request, Response] {

  def apply(request: IORequest, service: Service[Request, Response]): Future[IOResponse] = {
    val requestOut = Request.fromUnderlying(request)
    service(requestOut).map {
      responseIn =>
        responseIn.underlying
    }
  }

}
