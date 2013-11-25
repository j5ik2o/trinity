package org.sisioh.trinity.domain.mvc.server

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit
import org.sisioh.trinity.domain.mvc.{Environment, GlobalSettings}
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}
import org.sisioh.trinity.domain.mvc.filter.Filter

trait Server extends ServiceBuilder {

  val serverConfig: ServerConfig

  val action: Option[Action[Request, Response]]

  val filter: Option[Filter[Request, Response, Request, Response]]

  def start(environment: Environment.Value)(implicit executor: ExecutionContext): Future[Unit]

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