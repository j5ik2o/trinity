package org.sisioh.trinity.domain.mvc

import java.util.concurrent.Executors
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.Request
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPatternParser
import org.sisioh.trinity.domain.mvc.routing.pathpattern.SinatraPathPatternParser
import org.sisioh.trinity.domain.mvc.server.{Server, ServerConfigLoader}
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration


/**
 * サーバを起動させるためのトレイト。
 */
trait Bootstrap {

  protected implicit val executor = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  protected val applicationId: String = "."

  protected val environment: Environment.Value = Environment.Development

  protected lazy val configuration = ServerConfigLoader.loadConfiguration(applicationId, environment)

  protected lazy val serverConfig = ServerConfigLoader.loadServerConfig(configuration)

  protected implicit val globalSettings: Option[GlobalSettings[Request, Response]] = None

  protected implicit val pathPatternParser: PathPatternParser = SinatraPathPatternParser()

  protected val routingFilter: Option[RoutingFilter] = None

  protected val action: Option[Action[Request, Response]] = None

  protected def createServer: Server = Server(
    serverConfig = serverConfig,
    action = action,
    filter = routingFilter,
    globalSettings = globalSettings
  )

  protected lazy val server = createServer

  def start(): Future[Unit] = server.start(environment)

  def stop(): Future[Unit] = server.stop()

  def await(future: Future[Unit], duration: Duration = Duration.Inf): Unit =
    Await.result(future, duration)

  def startWithAwait(): Unit = await(start())

}
