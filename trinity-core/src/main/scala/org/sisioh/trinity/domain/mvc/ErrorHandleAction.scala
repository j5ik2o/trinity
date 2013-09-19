package org.sisioh.trinity.domain.mvc

import scala.concurrent.Future

case class ErrorHandleAction[Req <: Request, Rep <: Response]() extends Action[Req, Rep]{
  def apply(request: Req): Future[Rep] = ???
}
