package org.sisioh.trinity.domain.mvc

import scala.concurrent.{ExecutionContext, Future}

trait Server {

  def start()(implicit executor: ExecutionContext): Future[Unit]

  def stop()(implicit executor: ExecutionContext): Future[Unit]

}
