package org.sisioh.trinity

import com.twitter.finagle.Service
import com.twitter.util.{Try, Future}
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import org.sisioh.scala.toolbox.LoggingEx
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import org.jboss.netty.handler.codec.http.HttpVersion._
import scala.Some

class ControllerService(controllers: Controllers, globalSettingOpt: Option[GlobalSetting] = None)
  extends Service[FinagleRequest, FinagleResponse] with LoggingEx {

  protected def notFoundHandler = {
    request: Request =>
      globalSettingOpt.map {
        globalSetting =>
          globalSetting.notFound(request)
      }.getOrElse {
        render.status(404).plain("Not Found").toFuture
      }
  }

  protected def errorHandler = {
    request: Request =>
      globalSettingOpt.map {
        _.error(request)
      }.getOrElse {
        request.error match {
          case Some(ex) =>
            render.status(415).plain("No handler for this media type found").toFuture
          case _ =>
            render.status(500).plain("Something went wrong!").toFuture
        }
      }
  }

  def render = new Response

  def attemptRequest(rawRequest: FinagleRequest) = {
    val adaptedRequest = RequestAdapter(rawRequest)
    controllers.dispatch(rawRequest).getOrElse {
      ResponseAdapter(notFoundHandler(adaptedRequest))
    }
  }

  private def handleError(adaptedRequest: Request, t: Throwable) = {
    error("Internal Server Error", t)
    val newRequest = adaptedRequest.copy(error = Some(t))
    ResponseAdapter(errorHandler(newRequest))
  }

  def apply(rawRequest: FinagleRequest): Future[FinagleResponse] = {
    val adaptedRequest = RequestAdapter(rawRequest)
    Try {
      attemptRequest(rawRequest).rescue {
        case t: Throwable =>
          handleError(adaptedRequest, t)
      }
    }.rescue {
      case e: Exception =>
        Try(handleError(adaptedRequest, e))
    }.getOrElse {
      Future.value(FinagleResponse(HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR))
    }
  }


}
