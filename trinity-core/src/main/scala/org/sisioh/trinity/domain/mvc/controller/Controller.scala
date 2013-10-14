package org.sisioh.trinity.domain.mvc.controller

import java.util.UUID
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity, Entity}
import org.sisioh.trinity.domain.io.transport.codec.http.{ContentType, ResponseStatus}
import org.sisioh.trinity.domain.mvc.http.{Request, ResponseBuilder, Response}
import scala.concurrent.Future


trait Controller
  extends Entity[Identity[UUID]]
  with EntityCloneable[Identity[UUID], Controller]
  with Ordered[Controller] {

  val identity: Identity[UUID] = Identity(UUID.randomUUID())

  def compare(that: Controller): Int =
    identity.value.compareTo(that.identity.value)

  protected def redirect(location: String, responseOpt: Option[Response] = None): Future[Response] = {
    val responseBuilder = ResponseBuilder().
      withStatus(ResponseStatus.MovedPermanently).
      withHeader("Location", location)
    val response = responseOpt.map(responseBuilder.build(_)).getOrElse(responseBuilder.build)
    Future.successful(response)
  }

//  protected def respondTo(r: Request)(callback: PartialFunction[ContentType, Future[Response]]): Future[Response] = {
//    if (!r.routeParams.get("format").isEmpty) {
//      val format = r.routeParams("format")
//      val mime = ContentType.valueOf("." + format)
//      val contentType = ContentType.valueOf(mime).getOrElse(ContentType.All)
//      if (callback.isDefinedAt(contentType)) {
//        callback(contentType)
//      } else {
//        Future.failed(new RespondNotFoundException)
//      }
//    } else {
//      r.accepts.find {
//        mimeType =>
//          callback.isDefinedAt(mimeType)
//      }.map {
//        contentType =>
//          callback(contentType)
//      }.getOrElse {
//        Future.failed(new RespondNotFoundException)
//      }
//    }
//  }

}
