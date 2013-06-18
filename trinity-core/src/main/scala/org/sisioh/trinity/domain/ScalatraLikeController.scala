package org.sisioh.trinity.domain

import com.twitter.finagle.stats.{StatsReceiver, NullStatsReceiver}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http._
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.application.TrinityApplication
import com.twitter.finagle.http.Response

abstract class ScalatraLikeController(application: TrinityApplication, statsReceiver: StatsReceiver = NullStatsReceiver)
  extends Controller with LoggingEx {

  implicit protected val config = application.config
  implicit protected val pathParser = new SinatraPathPatternParser()

  val routeRepository = new RouteRepositoryOnMemory

  protected def get(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.GET, path)(callback)
  }

  protected def delete(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.DELETE, path)(callback)
  }

  protected def post(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.POST, path)(callback)
  }

  protected def put(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.PUT, path)(callback)
  }

  protected def head(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.HEAD, path)(callback)
  }

  protected def patch(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.PATCH, path)(callback)
  }

  protected val stats = statsReceiver.scope("Controller")

  protected def responseBuilder = new ResponseBuilder

  protected def redirect(location: String, message: String = "moved"): Future[Response]= {
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


  protected def addRoute(method: HttpMethod, path: String)(callback: Request => Future[Response]) {
    routeRepository.store(
      Route(method, path, Action {
        request =>
          stats.timeFuture("%s/Root/%s".format(method.toString, path.stripPrefix("/"))) {
            callback(request)
          }
      }
      )
    )
  }
}
