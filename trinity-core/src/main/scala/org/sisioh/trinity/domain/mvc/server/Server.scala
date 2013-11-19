package org.sisioh.trinity.domain.mvc.server

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit
import org.sisioh.trinity.domain.mvc.{GlobalSettings, Filter}
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

trait Server {

  def registerFilters(filters: Seq[Filter[Request, Response, Request, Response]])
                     (implicit executor: ExecutionContext): Unit

  def registerFilter(filter: Filter[Request, Response, Request, Response])
                    (implicit executor: ExecutionContext): Unit

  def start()(implicit executor: ExecutionContext): Future[Unit]

  def stop()(implicit executor: ExecutionContext): Future[Unit]

  def isStarted: Boolean
}

object Server {

  val defaultName = "trinity"

  val defaultAwaitDuration = Duration(5, TimeUnit.SECONDS)

  val defaultBindAddress = new InetSocketAddress(7070)

  def apply(serverConfig: ServerConfig = ServerConfig(),
            action: Option[Action[Request, Response]] = None,
            filter: Option[Filter[Request, Response, Request, Response]] = None,
            globalSettings: Option[GlobalSettings[Request, Response]] = None)
           (implicit executor: ExecutionContext): Server =
    new ServerImpl(serverConfig, action, filter, globalSettings)

}