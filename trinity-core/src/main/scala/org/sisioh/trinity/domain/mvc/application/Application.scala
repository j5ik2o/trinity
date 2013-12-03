package org.sisioh.trinity.domain.mvc.application

import org.sisioh.config.Configuration
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.server.ServerConfigLoader
import org.sisioh.trinity.domain.mvc.{GlobalSettings, Environment}
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import org.sisioh.scala.toolbox.LoggingEx

trait Application extends LoggingEx {

  protected lazy val applicationId: String = "."

  protected val environment: Environment.Value

  protected lazy val configuration: Configuration = withDebugScope("configuration"){
    scopedDebug(s"applicationId = $applicationId, environment = $environment")
    ServerConfigLoader.loadConfiguration(applicationId, environment).get
  }

  protected lazy val serverConfig = withDebugScope("serverConfig"){
    scopedDebug(s"applicationId = $applicationId, configuration = $configuration")
    ServerConfigLoader.loadServerConfig(configuration)
  }

  protected implicit val globalSettings: Option[GlobalSettings[Request, Response]] = None

  def start(): Future[Unit]

  def stop(): Future[Unit]

  def await(future: Future[Unit], duration: Duration = Duration.Inf): Unit

  def startWithAwait(): Unit

}
