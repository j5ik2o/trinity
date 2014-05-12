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
package org.sisioh.trinity.domain.mvc.server

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.filter.Filter
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.{Environment, GlobalSettings}
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

/**
 * Represents the trait for the server.
 */
trait Server extends ServiceBuilder {

  val serverConfig: ServerConfig

  val action: Option[Action[Request, Response]]

  val filter: Option[Filter[Request, Response, Request, Response]]

  def start(environment: Environment.Value)(implicit executor: ExecutionContext): Future[Unit]

  def stop()(implicit executor: ExecutionContext): Future[Unit]

  def isStarted: Boolean

}

/**
 * Represents the companion object for [[Server]].
 */
object Server {

  /**
   * The default name for [[Server]]
   */
  val defaultName = "trinity"

  /**
   * The duration to await for [[Server]]
   */
  val defaultAwaitDuration = Duration(5, TimeUnit.SECONDS)

  /**
   * The bind address for [[Server]]
   */
  val defaultBindAddress = new InetSocketAddress(7070)

  def apply(serverConfig: ServerConfig = ServerConfig(),
            action: Option[Action[Request, Response]] = None,
            filter: Option[Filter[Request, Response, Request, Response]] = None,
            globalSettings: Option[GlobalSettings[Request, Response]] = None)
           (implicit executor: ExecutionContext): Server =
    new ServerImpl(serverConfig, action, filter, globalSettings)

}