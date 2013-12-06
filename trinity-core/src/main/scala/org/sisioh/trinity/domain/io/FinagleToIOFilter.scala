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
 * Finagleのためのフィルター。
 *
 * FinagleとIORequest, IOResponseを扱うサービスとの橋渡しを行う。
 */
case class FinagleToIOFilter()
  extends Filter[Request, Response, IORequest, IOResponse] {

  def apply(request: Request, service: Service[IORequest, IOResponse]): Future[Response] = {
    Stats.timeFutureNanos("FinagleToIOFilter")(service(IORequest(request)).map(_.toUnderlyingAsFinagle))
  }

}
