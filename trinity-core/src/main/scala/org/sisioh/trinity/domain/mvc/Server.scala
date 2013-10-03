package org.sisioh.trinity.domain.mvc

import scala.concurrent.{ExecutionContext, Future}
import java.net.SocketAddress

trait Server {

  def registerFilters(filters: Seq[Filter[Request, Response, Request, Response]])(implicit executor: ExecutionContext): Unit

  def registerFilter(filter: Filter[Request, Response, Request, Response])(implicit executor: ExecutionContext): Unit

  def start()(implicit executor: ExecutionContext): Future[Unit]

  def stop()(implicit executor: ExecutionContext): Future[Unit]

}

object Server {

  def apply(bindAddress: SocketAddress,
            name: String = "trinity",
            actionOpt: Option[Action[Request, Response]] = None,
            globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None): Server =
    new ServerImpl(bindAddress, name, actionOpt, globalSettingsOpt)

}