package org.sisioh.trinity

import com.twitter.finagle.stats.{StatsReceiver, NullStatsReceiver}
import com.twitter.util.Future
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import org.jboss.netty.handler.codec.http._
import collection.mutable.ListBuffer
import org.sisioh.scala.toolbox.LoggingEx

class Controller(statsReceiver: StatsReceiver = NullStatsReceiver) extends LoggingEx {

  val routes = new RouteVector[(HttpMethod, PathPattern, Request => Future[Response])]


  def get(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.GET, path)(callback)
  }

  def delete(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.DELETE, path)(callback)
  }

  def post(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.POST, path)(callback)
  }

  def put(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.PUT, path)(callback)
  }

  def head(path: String)(callback: Request => Future[Response]) {
    addRoute(HttpMethod.HEAD, path)(callback)
  }

  def patch(path: String)(callback: Request => Future[Response]) {
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
    val req = RequestAdapter(request)
    findRouteAndMatch(req, method) match {
      case Some((method, pattern, callback)) =>
        Some(ResponseAdapter(callback(req)))
      case None =>
        orCallback(request)
    }
  }

  def extractParams(request: Request, xs: Tuple2[_, _]) = {
    request.routeParams += (xs._1.toString -> xs._2.asInstanceOf[ListBuffer[String]].head.toString)
  }

  def findRouteAndMatch(request: Request, method: HttpMethod) = {
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

  def render = new Response

  def redirect(location: String, message: String = "moved") = {
    render.plain(message).status(301).header("Location", location)
  }

  def respondTo(r: Request)(callback: PartialFunction[ContentType, Future[Response]]): Future[Response] = {
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

  def addRoute(method: HttpMethod, path: String)(callback: Request => Future[Response]) {
    val regex = SinatraPathPatternParser(path)
    routes.add((method, regex, (r) => {
      stats.timeFuture("%s/Root/%s".format(method.toString, path.stripPrefix("/"))) {
        callback(r)
      }
    }))
  }
}
