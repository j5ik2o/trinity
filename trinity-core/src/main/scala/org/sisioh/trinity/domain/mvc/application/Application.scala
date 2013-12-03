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

import org.sisioh.config.Configuration
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.server.ServerConfig
import org.sisioh.trinity.domain.mvc.{GlobalSettings, Environment}
import scala.concurrent.Future
import scala.concurrent.duration.Duration

/**
 * アプリケーションを表すトレイト。
 */
trait Application extends LoggingEx {

  protected lazy val applicationId: String = "."

  protected val environment: Environment.Value

  protected val configuration: Configuration

  protected val serverConfig: ServerConfig

  protected implicit val globalSettings: Option[GlobalSettings[Request, Response]] // = None

  def start(): Future[Unit]

  def stop(): Future[Unit]

  def await(future: Future[Unit], duration: Duration = Duration.Inf): Unit

  def startWithAwait(): Unit

}
