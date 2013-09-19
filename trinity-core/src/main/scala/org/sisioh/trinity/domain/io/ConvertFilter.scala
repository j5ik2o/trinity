package org.sisioh.trinity.domain.io

import com.twitter.finagle.http.{Response, Request}
import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Response => IOResponse}
import scala.concurrent.{ExecutionContext, Future}

case class ConvertFilter()(implicit executor: ExecutionContext) extends Filter[Request, Response, IORequest, IOResponse] {

  def apply(request: Request, service: Service[IORequest, IOResponse]): Future[Response] = {
    service(request).map(IOResponse.toFinagle)
  }

}
