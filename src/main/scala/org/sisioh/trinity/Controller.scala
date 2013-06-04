package org.sisioh.trinity

import com.twitter.finagle.stats.{StatsReceiver, NullStatsReceiver}
import com.twitter.util.Future
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import org.jboss.netty.handler.codec.http._
import collection.mutable.ListBuffer
import org.sisioh.scala.toolbox.LoggingEx

class Controller(statsReceiver: StatsReceiver = NullStatsReceiver) extends LoggingEx {

  val routes = new RouteVector[(HttpMethod, PathPattern, RequestAdaptor => Future[ResponseBuilder])]


  def get(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.GET, path)(callback)
  }

  def delete(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.DELETE, path)(callback)
  }

  def post(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.POST, path)(callback)
  }

  def put(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.PUT, path)(callback)
  }

  def head(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.HEAD, path)(callback)
  }

  def patch(path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    addRoute(HttpMethod.PATCH, path)(callback)
  }

  def dispatch(request: FinagleRequest): Option[Future[FinagleResponse]] = {
    logger.info("%s %s".format(request.method, request.uri))
    dispatchRouteOrCallback(request, request.method, (request) => {
      // fallback to GET for 404'ed GET requests (curl -I support)
      if (request.method == HttpMethod.HEAD) {
        dispatchRouteOrCallback(request, HttpMethod.GET, (request) => None)
      } else {
        return None
      }
    })
  }

  def dispatchRouteOrCallback(request: FinagleRequest, method: HttpMethod,
                              orCallback: FinagleRequest => Option[Future[FinagleResponse]]): Option[Future[FinagleResponse]] = {
    val req = RequestAdaptor(request)
    findRouteAndMatch(req, method) match {
      case Some((method, pattern, callback)) =>
        Some(ResponseAdapter(callback(req)))
      case None =>
        orCallback(request)
    }
  }

  def extractParams(request: RequestAdaptor, xs: Tuple2[_, _]) = {
    request.copy(routeParams = request.routeParams + (xs._1.toString -> xs._2.asInstanceOf[ListBuffer[String]].head.toString))
  }

  def findRouteAndMatch(request: RequestAdaptor, method: HttpMethod) = {
    var thematch: Option[Map[_, _]] = None

    routes.vector.find(route => route match {
      case (_method, pattern, callback) =>
        thematch = pattern(request.path.split('?').head)
        if (thematch.orNull != null && _method == method) {
          thematch.orNull.foreach(xs => extractParams(request, xs))
          true
        } else {
          false
        }
    })
  }

  val stats = statsReceiver.scope("Controller")

  def render = new ResponseBuilder

  def redirect(location: String, message: String = "moved") = {
    render.withPlain(message).withStatus(301).withHeader("Location", location)
  }

  def respondTo(r: RequestAdaptor)(callback: PartialFunction[ContentType, Future[ResponseBuilder]]): Future[ResponseBuilder] = {
    if (!r.routeParams.get("format").isEmpty) {
      val format = r.routeParams("format")
      val mime = FileService.getContentType("." + format)
      val contentType = ContentType.valueOf(mime).getOrElse(ContentType.All)
      if (callback.isDefinedAt(contentType)) {
        callback(contentType)
      } else {
        throw new Exception
      }
    } else {
      r.accepts.find {
        mimeType =>
          callback.isDefinedAt(mimeType)
      } match {
        case Some(contentType) =>
          callback(contentType)
        case None =>
          throw new Exception
      }
    }
  }

  def addRoute(method: HttpMethod, path: String)(callback: RequestAdaptor => Future[ResponseBuilder]) {
    val regex = SinatraPathPatternParser(path)
    routes.add((method, regex, (r) => {
      stats.timeFuture("%s/Root/%s".format(method.toString, path.stripPrefix("/"))) {
        callback(r)
      }
    }))
  }
}
