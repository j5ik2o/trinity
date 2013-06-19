package org.sisioh.trinity.domain.controller

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain._
import org.sisioh.trinity.domain.routing.{RouteRepositoryOnMemory, SinatraPathPatternParser, PathPatternParser}
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.http.{Request, ContentType}

abstract class AbstractController()(implicit val application: TrinityApplication) extends Controller {

  implicit protected val config: Config = application.config

  implicit protected val pathParser: PathPatternParser = new SinatraPathPatternParser()

  val routeRepository: RouteRepositoryOnMemory = new RouteRepositoryOnMemory

  protected def redirect(location: String, message: String = "moved"): Future[Response] = {
    responseBuilder.withPlain(message).withStatus(301).withHeader("Location", location).toFuture
  }

  protected def respondTo(r: Request)(callback: PartialFunction[ContentType, Future[Response]]): Future[Response] = {
    if (!r.routeParams.get("format").isEmpty) {
      val format = r.routeParams("format")
      val mime = ContentType.getContentType("." + format)
      val contentType = ContentType.valueOf(mime).getOrElse(ContentType.All)
      if (callback.isDefinedAt(contentType)) {
        callback(contentType)
      } else {
        throw new CallbackNotFoundException
      }
    } else {
      r.accepts.find {
        mimeType =>
          callback.isDefinedAt(mimeType)
      }.map {
        contentType =>
          callback(contentType)
      }.getOrElse {
        throw new CallbackNotFoundException
      }
    }
  }

}
