package org.sisioh.trinity.domain.io

import com.twitter.finagle.http.{Response, Request}
import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Response => IOResponse}
import com.twitter.finagle.{Service, Filter}
import com.twitter.util.Future
import com.twitter.ostrich.stats.Stats

case class FinagleToIOFilter()
  extends Filter[Request, Response, IORequest, IOResponse] {

  def apply(request: Request, service: Service[IORequest, IOResponse]): Future[Response] = {
    Stats.timeFutureNanos("FinagleToIOFilter")(service(request).map(IOResponse.toFinagle))
  }

}
