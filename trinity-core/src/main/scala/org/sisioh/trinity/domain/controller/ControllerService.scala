package org.sisioh.trinity.domain.controller

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.util.{Return, Try, Future}
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpResponseStatus}
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.http.{TrinityResponseImplicitSupport, TrinityResponseBuilder, TrinityRequest}
import org.sisioh.trinity.domain.routing.{RouteId, Route}

class ControllerService(application: TrinityApplication, globalSettingOpt: Option[GlobalSettings] = None)
  extends Service[FinagleRequest, FinagleResponse] with LoggingEx with TrinityResponseImplicitSupport {

  protected def notFoundHandler: (TrinityRequest) => Future[FinagleResponse] = {
    request: TrinityRequest =>
      globalSettingOpt.map {
        globalSetting =>
          globalSetting.notFound(request)
      }.getOrElse {
        if (application.config.isRecoveryActionNotFound) {
          builder.
            withStatus(HttpResponseStatus.NOT_FOUND).
            withPlain("Not Found").toTrinityResponseFuture
        } else {
          Future.exception(ActionNotFoundException(Some(request.toString())))
        }
      }
  }

  protected def errorHandler: (TrinityRequest) => Future[FinagleResponse] = {
    request: TrinityRequest =>
      globalSettingOpt.map {
        _.error(request)
      }.getOrElse {
        request.error match {
          case Some(ex) =>
            if (application.config.isRecoveryError) {
              builder.withStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR).withPlain("Internal Server Error").toTrinityResponseFuture
            } else {
              Future.exception(TrinityException(Some(s"Error at $request"), Some(ex)))
            }
          case _ =>
            builder.withStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR).withPlain("Internal Server Error").toTrinityResponseFuture
        }
      }
  }

  def builder: TrinityResponseBuilder = new TrinityResponseBuilder

  def dispatch(request: TrinityRequest): Option[Future[FinagleResponse]] = {
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
  (request: TrinityRequest,
   method: HttpMethod,
   orCallback: TrinityRequest => Option[Future[FinagleResponse]])
  : Option[Future[FinagleResponse]] = {
    findRoute(request, method).map {
      case Route(RouteId(method, pattern), _, action) =>
        val routeParamsOpt = pattern(request.path.split('?').head)
        val newReq = routeParamsOpt.map {
          routeParams =>
            request.copy(routeParams = request.routeParams ++ routeParams)
        }.getOrElse(request)
        Some(action(newReq))
    }.getOrElse {
      orCallback(request)
    }
  }

  def findRoute(request: TrinityRequest, method: HttpMethod): Option[Route] = {
    application.routeRepository.find {
      case Route(RouteId(m, p), controllerId, _) =>
        val hasController = application.controllerRepository.contains(controllerId)
        val routeParamsOpt = p(request.path.split('?').head)
        if (hasController.toOption.get && routeParamsOpt.isDefined && m == method) true else false
    }
  }

  protected def attemptRequest(request: TrinityRequest): Future[FinagleResponse] = {
    dispatch(request).getOrElse {
      notFoundHandler(request)
    }
  }

  protected def handleError(adaptedRequest: TrinityRequest, throwable: Throwable): Future[FinagleResponse] = {
    error("Internal Server Error", throwable)
    val newRequest = adaptedRequest.copy(error = Some(throwable))
    errorHandler(newRequest)
  }

  def apply(rawRequest: FinagleRequest): Future[FinagleResponse] = {
    val adaptedRequest = TrinityRequest(rawRequest)
    Try {
      attemptRequest(adaptedRequest).rescue {
        case throwable: Throwable =>
          handleError(adaptedRequest, throwable)
      }
    }.rescue {
      case throwable: Exception =>
        Try(handleError(adaptedRequest, throwable))
    }.rescue {
      case throwable: Exception =>
        Return(Future.exception(throwable))
    }.getOrElse {
      Future.exception(TrinityException(Some("Other Exception")))
    }
  }


}
