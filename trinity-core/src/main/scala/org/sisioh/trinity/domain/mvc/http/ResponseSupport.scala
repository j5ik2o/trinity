package org.sisioh.trinity.domain.mvc.http

import scala.concurrent.Future
import org.sisioh.trinity.domain.io.http.{MimeTypes, ContentType, HeaderNames, ResponseStatus}

trait ResponseSupport {

  protected def responseBuilder = ResponseBuilder()

  protected def redirect(location: String, response: Option[Response] = None): Future[Response] = {
    val responseBuilder = ResponseBuilder().
      withResponseStatus(ResponseStatus.MovedPermanently).
      withHeader(HeaderNames.Location, location)
    val resp = response.map(responseBuilder.build(_)).getOrElse(responseBuilder.build)
    Future.successful(resp)
  }

  protected def respondTo(request: Request)(callback: PartialFunction[ContentType, Future[Response]]): Future[Response] = {
    if (!request.routeParams.get("format").isEmpty) {
      val format = request.routeParams("format")
      val mime = MimeTypes.fileExtensionOf("." + format)
      val contentType = ContentType.valueOf(mime).getOrElse(ContentType.All)
      if (callback.isDefinedAt(contentType)) {
        callback(contentType)
      } else {
        Future.failed(new RespondNotFoundException)
      }
    } else {
      request.accepts.find {
        mimeType =>
          callback.isDefinedAt(mimeType)
      }.map {
        contentType =>
          callback(contentType)
      }.getOrElse {
        Future.failed(new RespondNotFoundException)
      }
    }
  }

}
