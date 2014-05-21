/*
 * Copyright 2013 Sisioh Project and others. (http://sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.trinity.domain.mvc.application

import java.util.concurrent.Executors
import org.sisioh.config.Configuration
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.Request
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPatternParser
import org.sisioh.trinity.domain.mvc.routing.pathpattern.SinatraPathPatternParser
import org.sisioh.trinity.domain.mvc.server.{ServerConfigLoader, Server}
import org.sisioh.trinity.domain.mvc.{Environment, GlobalSettings}
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

/**
 * Represents the trait to support the server booting.
 */
trait Bootstrap {
  this: Application =>

  protected implicit val executor: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  protected implicit val pathPatternParser: PathPatternParser = SinatraPathPatternParser()

  override protected val environment: Environment.Value

  protected val configPrefixName: Option[String] = None

  override protected lazy val configuration: Configuration = withDebugScope("configuration") {
    scopedDebug(s"applicationId = $applicationId, environment = $environment")
    ServerConfigLoader.loadConfiguration(applicationId, environment).get
  }

  override protected lazy val serverConfig = withDebugScope("serverConfig") {
    scopedDebug(s"applicationId = $applicationId, configuration = $configuration")
    ServerConfigLoader.loadAsServerConfig(configuration, configPrefixName)
  }

  override protected implicit val globalSettings: Option[GlobalSettings[Request, Response]] = None

  protected lazy val routingFilter: Option[RoutingFilter] = None

  protected lazy val action: Option[Action[Request, Response]] = None

  protected def createServer: Server = Server(
    serverConfig = serverConfig,
    action = action,
    filter = routingFilter,
    globalSettings = globalSettings
  )

  protected lazy val server = createServer

  override def start(): Future[Unit] = {
    server.start(environment)
  }

  override def stop(): Future[Unit] = server.stop()

  override def await(future: Future[Unit], duration: Duration = Duration.Inf): Unit =
    Await.result(future, duration)

  override def startWithAwait(): Unit = await(start())

}
