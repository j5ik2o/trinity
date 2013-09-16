package org.sisioh.trinity.domain.mvc

import scala.concurrent.Future

trait Action[-Req, +Rep] extends (Req => Future[Rep]) {

  def apply(request: Req): Future[Rep]

}
