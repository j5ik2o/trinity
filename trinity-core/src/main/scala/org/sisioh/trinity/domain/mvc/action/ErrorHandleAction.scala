package org.sisioh.trinity.domain.mvc.action

import org.sisioh.trinity.domain.io.transport.codec.http.{ResponseStatus, Version}
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import scala.concurrent.Future

case object ErrorHandleAction extends Action[Request, Response] {

  def apply(request: Request): Future[Response] =
    Future.successful(Response(Version.Http11, ResponseStatus.InternalServerError))

}
