package org.sisioh.trinity.domain.mvc.controller

import java.util.UUID
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity, Entity}
import org.sisioh.trinity.domain.io.http.{ContentType, ResponseStatus}
import org.sisioh.trinity.domain.mvc.http.{ResponseSupport, Request, ResponseBuilder, Response}
import scala.concurrent.Future

/**
 * [[org.sisioh.trinity.domain.mvc.action.Action]]の集合を保持できるコントローラ。
 *
  [[org.sisioh.trinity.domain.mvc.action.Action]]に取ってRequestやResponseを処理する際に便利な機能を提供する。
 * [[org.sisioh.trinity.domain.mvc.action.Action]]にとって[[org.sisioh.trinity.domain.mvc.controller.Controller]]は必須ではない。
 */
trait Controller
  extends Entity[Identity[UUID]]
  with EntityCloneable[Identity[UUID], Controller]
  with Ordered[Controller] with ResponseSupport {

  val identity: Identity[UUID] = Identity(UUID.randomUUID())

  def compare(that: Controller): Int =
    identity.value.compareTo(that.identity.value)

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
