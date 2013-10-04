package org.sisioh.trinity.domain.mvc.action

import scala.concurrent.Future

trait Action[-Req, +Rep] extends (Req => Future[Rep]) {

  def apply(request: Req): Future[Rep]

}

object Action {

  def apply[Req, Rep](f: (Req) => Future[Rep]): Action[Req, Rep] = new Action[Req, Rep] {
    def apply(request: Req): Future[Rep] = f(request)
  }

}
