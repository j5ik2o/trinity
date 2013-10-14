package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.mvc.server.{ServerConfigLoader, Server}
import scala.concurrent.{Future, ExecutionContext, Await}
import scala.concurrent.duration.Duration
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{SinatraPathPatternParser, PathPatternParser}
import java.util.concurrent.Executors
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.action.Action

trait Bootstrap {

  protected implicit val executor = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  protected val environment: Environment.Value

  protected lazy val serverConfig = ServerConfigLoader.load(environment)

  protected implicit val globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None

  protected implicit val pathPatternParser: PathPatternParser = SinatraPathPatternParser()

  protected val routingFilterOpt: Option[RoutingFilter] = None

  protected val actionOpt: Option[Action[Request, Response]] = None

  protected def createServer: Server = Server(
    serverConfig = serverConfig,
    actionOpt = actionOpt,
    filterOpt = routingFilterOpt,
    globalSettingsOpt = globalSettingsOpt)

  protected lazy val server = createServer

  def start() =
    server.start()

  def stop() =
    server.stop()

  def await(future: Future[Unit], duration: Duration = Duration.Inf): Unit =
    Await.result(future, duration)

}
