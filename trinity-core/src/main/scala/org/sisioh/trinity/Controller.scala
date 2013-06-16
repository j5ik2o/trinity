package org.sisioh.trinity

import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.finagle.stats.{StatsReceiver, NullStatsReceiver}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http._
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain._
import org.sisioh.trinity.domain.RouteId
import org.sisioh.trinity.domain.Route


abstract class Controller(application: TrinityApplication, statsReceiver: StatsReceiver = NullStatsReceiver) extends LoggingEx {

  val config = application.config


  protected def get(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.GET, path)(Action(callback))
  }

  protected def delete(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.DELETE, path)(Action(callback))
  }

  protected def post(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.POST, path)(Action(callback))
  }

  protected def put(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.PUT, path)(Action(callback))
  }

  protected def head(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.HEAD, path)(Action(callback))
  }

  protected def patch(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.PATCH, path)(Action(callback))
  }

  def dispatch(request: FinagleRequest): Option[Future[FinagleResponse]] = {
    logger.info("%s %s".format(request.method, request.uri))
    dispatchRouteOrCallback(request, request.method, (request) => {
      // fallback to GET for 404'ed GET requests (curl -I support)
      if (request.method == HttpMethod.HEAD) {
        dispatchRouteOrCallback(request, HttpMethod.GET, (request) => None)
      } else {
        None
      }
    })
  }

  private def dispatchRouteOrCallback
  (request: FinagleRequest,
   method: HttpMethod,
   orCallback: FinagleRequest => Option[Future[FinagleResponse]])
  : Option[Future[FinagleResponse]] = {
    val requestAdaptor = RequestAdaptor(request)
    findRoute(requestAdaptor, method).map {
      case Route(RouteId(method, pattern), callback) =>
        val routeParamsOpt = pattern(request.path.split('?').head)
        val newReq = routeParamsOpt.map {
          routeParams =>
            requestAdaptor.copy(routeParams = requestAdaptor.routeParams ++ routeParams)
        }.getOrElse(requestAdaptor)
        Some(ResponseAdapter(callback(newReq)))
    }.getOrElse {
      orCallback(request)
    }
  }


  def findRoute(request: RequestAdaptor, method: HttpMethod): Option[Route] = {
    application.routeRepository.find {
      case Route(RouteId(m, p), _) =>
        val routeParamsOpt = p(request.path.split('?').head)
        if (routeParamsOpt.isDefined && m == method) true else false
    }
  }

  private val stats = statsReceiver.scope("Controller")

  protected def responseBuilder = new ResponseBuilder

  protected def redirect(location: String, message: String = "moved"): ResponseBuilder = {
    responseBuilder.withPlain(message).withStatus(301).withHeader("Location", location)
  }

  protected def respondTo(r: RequestAdaptor)(callback: PartialFunction[ContentType, Future[ResponseBuilder]]): Future[ResponseBuilder] = {
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

  protected def addRoute(method: HttpMethod, path: String)(callback: Action) {
    val regex = SinatraPathPatternParser(path)
    application.routeRepository.store(
      Route(RouteId(method, regex), Action {
        request =>
          stats.timeFuture("%s/Root/%s".format(method.toString, path.stripPrefix("/"))) {
            callback(request)
          }
      }
      )
    )
  }
}
