package org.sisioh.trinity.domain.mvc.action

import org.sisioh.trinity.domain.io.http.{ResponseStatus, ProtocolVersion}
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import scala.concurrent.Future

/**
 * NOT_FOUND(404)を返す[[org.sisioh.trinity.domain.mvc.action.Action]]。
 */
case object NotFoundHandleAction extends Action[Request, Response] {

  def apply(request: Request): Future[Response] =
    Future.successful(Response(ResponseStatus.NotFound, ProtocolVersion.Http11))

}
