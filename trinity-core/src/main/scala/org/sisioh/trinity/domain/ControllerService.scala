package org.sisioh.trinity.domain

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.util.{Try, Future}
import org.jboss.netty.handler.codec.http.HttpVersion._
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpResponseStatus}
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.application.TrinityApplication

class ControllerService(application: TrinityApplication, globalSettingOpt: Option[GlobalSetting] = None)
  extends Service[FinagleRequest, FinagleResponse] with LoggingEx {

  protected def notFoundHandler = {
    request: RequestAdaptor =>
      globalSettingOpt.map {
        globalSetting =>
          globalSetting.notFound(request)
      }.getOrElse {
        render.withStatus(404).withPlain("Not Found").toFuture
      }
  }

  protected def errorHandler = {
    request: RequestAdaptor =>
      globalSettingOpt.map {
        _.error(request)
      }.getOrElse {
        request.error match {
          case Some(ex) =>
            render.withStatus(415).withPlain("No handler for this media type found").toFuture
          case _ =>
            render.withStatus(500).withPlain("Something went wrong!").toFuture
        }
      }
  }

  def render = new ResponseBuilder

  def dispatch(request: RequestAdaptor): Option[Future[FinagleResponse]] = {
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
  (request: RequestAdaptor,
   method: HttpMethod,
   orCallback: RequestAdaptor => Option[Future[FinagleResponse]])
  : Option[Future[FinagleResponse]] = {
    val requestAdaptor = RequestAdaptor(request)
    findRoute(requestAdaptor, method).map {
      case Route(RouteId(method, pattern), callback) =>
        val routeParamsOpt = pattern(request.path.split('?').head)
        val newReq = routeParamsOpt.map {
          routeParams =>
            requestAdaptor.copy(routeParams = requestAdaptor.routeParams ++ routeParams)
        }.getOrElse(requestAdaptor)
        Some(callback(newReq))
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

  protected def attemptRequest(request: RequestAdaptor) = {
    dispatch(request).getOrElse {
      notFoundHandler(request)
    }
  }

  protected def handleError(adaptedRequest: RequestAdaptor, throwable: Throwable) = {
    error("Internal Server Error", throwable)
    val newRequest = adaptedRequest.copy(error = Some(throwable))
    errorHandler(newRequest)
  }

  def apply(rawRequest: FinagleRequest): Future[FinagleResponse] = {
    val adaptedRequest = RequestAdaptor(rawRequest)
    Try {
      attemptRequest(adaptedRequest).rescue {
        case throwable: Throwable =>
          handleError(adaptedRequest, throwable)
      }
    }.rescue {
      case e: Exception =>
        Try(handleError(adaptedRequest, e))
    }.getOrElse {
      Future.value(FinagleResponse(HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR))
    }
  }


}
