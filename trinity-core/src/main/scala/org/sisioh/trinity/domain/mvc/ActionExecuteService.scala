package org.sisioh.trinity.domain.mvc

import org.sisioh.scala.toolbox.LoggingEx
import com.twitter.finagle.Service
import com.twitter.util.Future
import org.sisioh.trinity.infrastructure.util.FutureConverters._
import scala.concurrent.ExecutionContext

case class ActionExecuteService(implicit executor: ExecutionContext)
  extends Service[Request, Response] with LoggingEx {

  def apply(request: Request): Future[Response] = request.execute.toTwitter

}
