package org.sisioh.trinity.domain.io

import org.sisioh.trinity.domain.io.http.{ Request => IORequest }
import org.sisioh.trinity.domain.io.http.Request.toTrinity
import org.sisioh.trinity.domain.io.http.{ Response => IOResponse }
import com.twitter.finagle.Filter
import com.twitter.finagle.Service
import com.twitter.finagle.http.Request
import com.twitter.finagle.http.Response
import com.twitter.util.Future
import com.twitter.ostrich.stats.Stats

case class FinagleToIOFilter()
  extends Filter[Request, Response, IORequest, IOResponse] {

  def apply(request: Request, service: Service[IORequest, IOResponse]): Future[Response] = {
    Stats.timeFutureNanos("FinagleToIOFilter")(service(request).map(IOResponse.toFinagle))
  }

}
