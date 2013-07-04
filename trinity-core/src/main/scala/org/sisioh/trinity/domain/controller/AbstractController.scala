package org.sisioh.trinity.domain.controller

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.http.{TrinityRequest, ContentType}
import org.sisioh.trinity.domain.routing.{RouteRepositoryOnMemory, SinatraPathPatternParser, PathPatternParser}
import org.jboss.netty.handler.codec.http.HttpResponseStatus

abstract class AbstractController()(implicit val application: TrinityApplication)
  extends Controller {

  implicit protected val config: Config = application.config

  implicit protected val pathParser: PathPatternParser = new SinatraPathPatternParser()

  val routeRepository: RouteRepositoryOnMemory = new RouteRepositoryOnMemory

  val controllerRepository: ControllerRepository = new ControllerRepositoryOnMemory

  protected def redirect
  (location: String, message: String = "moved"): Future[Response] = {
    responseBuilder.withPlain(message).
      withStatus(HttpResponseStatus.MOVED_PERMANENTLY).
      withHeader("Location", location).toTrinityResponseFuture
  }

  protected def respondTo(r: TrinityRequest)(callback: PartialFunction[ContentType, Future[Response]]): Future[Response] = {
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
