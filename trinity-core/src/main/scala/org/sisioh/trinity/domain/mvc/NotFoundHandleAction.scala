package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.transport.codec.http.{ResponseStatus, Version}
import scala.concurrent.Future

case object NotFoundHandleAction extends Action[Request, Response] {

  def apply(request: Request): Future[Response] =
    Future.successful(Response(Version.Http11, ResponseStatus.NotFound))

}
