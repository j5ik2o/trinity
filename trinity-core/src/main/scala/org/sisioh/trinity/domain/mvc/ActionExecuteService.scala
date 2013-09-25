package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.Service
import org.sisioh.scala.toolbox.LoggingEx
import scala.concurrent.Future

case class ActionExecuteService() extends Service[Request, Response] with LoggingEx {

  def apply(request: Request): Future[Response] = request.execute

}
