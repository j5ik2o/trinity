package org.sisioh.trinity.domain.mvc.http

import scala.concurrent.Future
import org.sisioh.trinity.domain.io.http.{HeaderNames, ResponseStatus}

trait ResponseSupport {

  protected def redirect(location: String, responseOpt: Option[Response] = None): Future[Response] = {
    val responseBuilder = ResponseBuilder().
      withResponseStatus(ResponseStatus.MovedPermanently).
      withHeader(HeaderNames.Location, location)
    val response = responseOpt.map(responseBuilder.build(_)).getOrElse(responseBuilder.build)
    Future.successful(response)
  }

}
