package org.sisioh.trinity.domain.io

import com.twitter.finagle.Filter
import com.twitter.finagle.Service
import com.twitter.finagle.http.Request
import com.twitter.finagle.http.Response
import com.twitter.ostrich.stats.Stats
import com.twitter.util.Future
import org.sisioh.trinity.domain.io.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.http.{Response => IOResponse}

/**
 * Represents the filter as bridge from Finagle to Trinity.
 */
case class FinagleToIOFilter()
  extends Filter[Request, Response, IORequest, IOResponse] {

  override def apply(request: Request, service: Service[IORequest, IOResponse]): Future[Response] = {
    Stats.timeFutureNanos("FinagleToIOFilter")(
      service(IORequest(request)).map(_.toUnderlyingAsFinagle)
    )
  }

}
