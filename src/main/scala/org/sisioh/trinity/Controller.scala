package org.sisioh.trinity

import com.twitter.finagle.stats.{StatsReceiver, NullStatsReceiver}
import com.twitter.util.Future
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import org.jboss.netty.handler.codec.http._
import org.sisioh.scala.toolbox.LoggingEx

abstract class Controller(config: Config, statsReceiver: StatsReceiver = NullStatsReceiver) extends LoggingEx {

  protected val routes = new RouteVector[Route]

  protected def get(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.GET, path)(callback)
  }

  protected def delete(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.DELETE, path)(callback)
  }

  protected def post(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.POST, path)(callback)
  }

  protected def put(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.PUT, path)(callback)
  }

  protected def head(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.HEAD, path)(callback)
  }

  protected def patch(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.PATCH, path)(callback)
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
    findRouteAndMatch(requestAdaptor, method).map {
      case Route(method, pattern, callback) =>
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

  case class Route(method: HttpMethod, pathPattern: PathPattern, action: (RequestAdaptor) => Future[ResponseBuilder])

  private def findRouteAndMatch(request: RequestAdaptor, method: HttpMethod): Option[Route] = {
    routes.vector.find {
      case Route(m, p, _) =>
        val routeParamsOpt = p(request.path.split('?').head)
        if (routeParamsOpt.isDefined && m == method) true else false
    }
  }

  private val stats = statsReceiver.scope("Controller")

  protected def render = new ResponseBuilder

  protected def redirect(location: String, message: String = "moved"): ResponseBuilder = {
    render.withPlain(message).withStatus(301).withHeader("Location", location)
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

  protected def addRoute(method: HttpMethod, path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    val regex = SinatraPathPatternParser(path)
    routes.add(Route(method, regex, (r) => {
      stats.timeFuture("%s/Root/%s".format(method.toString, path.stripPrefix("/"))) {
        callback(r)
      }
    }))
  }
}
