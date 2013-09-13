package org.sisioh.trinity.domain

import scala.concurrent.Future

trait Service[-Req, +Rep] extends (Req => Future[Rep]) {

  def apply(request: Req): Future[Rep]

}
