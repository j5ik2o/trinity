package org.sisioh.trinity.domain.mvc.application

import java.util.concurrent.Executors
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.Request
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPatternParser
import org.sisioh.trinity.domain.mvc.routing.pathpattern.SinatraPathPatternParser
import org.sisioh.trinity.domain.mvc.server.Server
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

/**
 * サーバを起動させるためのトレイト。
 */
trait Bootstrap {
  this: Application =>

  protected implicit val executor = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  protected implicit val pathPatternParser: PathPatternParser = SinatraPathPatternParser()

  protected lazy val routingFilter: Option[RoutingFilter] = None

  protected lazy val action: Option[Action[Request, Response]] = None

  protected def createServer: Server = Server(
    serverConfig = serverConfig,
    action = action,
    filter = routingFilter,
    globalSettings = globalSettings
  )

  protected lazy val server = createServer

  def start(): Future[Unit] = {
    server.start(environment)
  }

  def stop(): Future[Unit] = server.stop()

  def await(future: Future[Unit], duration: Duration = Duration.Inf): Unit =
    Await.result(future, duration)

  def startWithAwait(): Unit = await(start())

}
