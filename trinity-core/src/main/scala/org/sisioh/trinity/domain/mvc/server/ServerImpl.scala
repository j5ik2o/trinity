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

import com.twitter.finagle.CodecFactory
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.builder.{Server => FinagleServer}
import com.twitter.finagle.http.Http
import com.twitter.finagle.http.RichHttp
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.finagle.tracing.{NullTracer, Tracer}
import com.twitter.ostrich.admin._
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.filter.Filter
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.{Environment, GlobalSettings}
import org.sisioh.trinity.util.DurationConverters._
import org.sisioh.trinity.util.FutureConverters._
import scala.concurrent._
import com.twitter.finagle.builder.ServerConfig.Yes

private[mvc]
class ServerImpl
(val serverConfig: ServerConfig,
 val action: Option[Action[Request, Response]],
 val filter: Option[Filter[Request, Response, Request, Response]],
 val globalSettings: Option[GlobalSettings[Request, Response]])
(implicit executor: ExecutionContext)
  extends Server with LoggingEx {

  private var finagleServer: Option[FinagleServer] = None

  protected def createTracer: Tracer = NullTracer

  protected def createRuntimeEnviroment: RuntimeEnvironment = new RuntimeEnvironment(this)

  private val defaultAdminHttpServicePort = 9990

  filter.foreach(registerFilter)

  /**
   * [[com.twitter.ostrich.admin.AdminHttpService]] を生成する。
   *
   * @param runtimeEnv [[com.twitter.ostrich.admin.RuntimeEnvironment]]
   * @return [[com.twitter.ostrich.admin.AdminHttpService]]
   */
  protected def createAdminHttpService(runtimeEnv: RuntimeEnvironment): AdminHttpService = withDebugScope("createAdminService") {
    val httpPort = serverConfig.statsPort.getOrElse(defaultAdminHttpServicePort)
    val serviceName = serverConfig.name
    scopedDebug(s"startPort = $httpPort, serviceName = $serviceName")
    AdminServiceFactory(
      httpPort,
      statsNodes = StatsFactory(
        reporters = JsonStatsLoggerFactory(serviceName = serverConfig.name) :: TimeSeriesCollectorFactory() :: Nil
      ) :: Nil
    )(runtimeEnv)
  }


  /**
   * コーデックを生成する。
   *
   * @return [[com.twitter.finagle.CodecFactory]]
   */
  protected def createCodec: CodecFactory[FinagleRequest, FinagleResponse] = {
    import com.twitter.conversions.storage._
    val http = Http()
    serverConfig.maxRequestSize.foreach {
      v =>
        http.maxRequestSize(v.megabytes)
    }
    serverConfig.maxResponseSize.foreach {
      v =>
        http.maxResponseSize(v.megabytes)
    }
    RichHttp[FinagleRequest](http)
  }

  private def configNewSSLEngine[Req, Rep, HasCodec, HasBindTo, HasName]
  (sb: ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes]): ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes] = {
    serverConfig.newSSLEngine.map {
      f =>
        sb.newFinagleSslEngine({
          () =>
            val r = f()
            com.twitter.finagle.ssl.Engine(r.self, r.handlesRenegotiation, r.certId)
        })
    }.getOrElse(sb)
  }

  private def configTls[Req, Rep, HasCodec, HasBindTo, HasName]
  (sb: ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes]): ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes] = {
    serverConfig.tlsConfig.map {
      tc =>
        sb.tls(
          tc.certificatePath,
          tc.keyPath,
          tc.caCertificatePath.orNull,
          tc.ciphers.orNull,
          tc.nextProtos.orNull
        )
    }.getOrElse(sb)
  }

  private def configMaxConcurrentRequests[Req, Rep, HasCodec, HasBindTo, HasName]
  (sb: ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes]): ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes] = {
    serverConfig.maxConcurrentRequests.map {
      v =>
        sb.maxConcurrentRequests(v)
    }.getOrElse(sb)
  }


  private def configHostConnectionMaxIdleTime[Req, Rep, HasCodec, HasBindTo, HasName]
  (sb: ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes]): ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes] = {
    serverConfig.hostConnectionMaxIdleTime.map {
      v =>
        sb.hostConnectionMaxIdleTime(v.toTwitter)
    }.getOrElse(sb)
  }

  private def configHostConnectionMaxLifeTime[Req, Rep, HasCodec, HasBindTo, HasName]
  (sb: ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes]): ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes] = {
    serverConfig.hostConnectionMaxLifeTime.map {
      v =>
        sb.hostConnectionMaxLifeTime(v.toTwitter)
    }.getOrElse(sb)
  }


  private def configRequestTimeout[Req, Rep, HasCodec, HasBindTo, HasName]
  (sb: ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes]): ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes] = {
    serverConfig.requestTimeout.map {
      v =>
        sb.requestTimeout(v.toTwitter)
    }.getOrElse(sb)
  }

  private def configReadTimeout[Req, Rep, HasCodec, HasBindTo, HasName]
  (sb: ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes]): ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes] = {
    serverConfig.readTimeout.map {
      v =>
        sb.readTimeout(v.toTwitter)
    }.getOrElse(sb)
  }

  private def configWriteCompletionTimeout[Req, Rep, HasCodec, HasBindTo, HasName]
  (sb: ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes]): ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes] = {
    serverConfig.writeCompletionTimeout.map {
      v =>
        sb.writeCompletionTimeout(v.toTwitter)
    }.getOrElse(sb)
  }

  private def configSendBufferSize[Req, Rep, HasCodec, HasBindTo, HasName]
  (sb: ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes]): ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes] = {
    serverConfig.sendBufferSize.map {
      v =>
        sb.sendBufferSize(v)
    }.getOrElse(sb)
  }

  private def configReceiveBufferSize[Req, Rep, HasCodec, HasBindTo, HasName]
  (sb: ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes]): ServerBuilder[Req, Rep, HasCodec, HasBindTo, Yes] = {
    serverConfig.receiveBufferSize.map {
      v =>
        sb.recvBufferSize(v)
    }.getOrElse(sb)
  }


  def start(environment: Environment.Value = Environment.Development)
           (implicit executor: ExecutionContext): Future[Unit] = future {
    withDebugScope("start") {
      require(finagleServer.isEmpty)
      info(s"aciton = $action, routingFilter = $filter")
      if (serverConfig.statsEnabled) {
        createAdminHttpService(createRuntimeEnviroment)
      }
      val service = buildService(environment, action)
      val bindAddress = serverConfig.bindAddress.getOrElse(Server.defaultBindAddress)
      info(s"bindAddress = $bindAddress")
      val name = serverConfig.name.getOrElse(Server.defaultName)
      info(s"name = $name")

      val defaultServerBuilder = ServerBuilder()
        .codec(createCodec)
        .bindTo(bindAddress)
        .tracer(createTracer)
        .name(name)

      val sb1 = configNewSSLEngine(defaultServerBuilder)
      val sb2 = configTls(sb1)

      val sb3 = configMaxConcurrentRequests(sb2)
      val sb4 = configHostConnectionMaxIdleTime(sb3)
      val sb5 = configHostConnectionMaxLifeTime(sb4)

      val sb6 = configRequestTimeout(sb5)
      val sb7 = configReadTimeout(sb6)
      val sb8 = configWriteCompletionTimeout(sb7)

      val sb9 = configSendBufferSize(sb8)
      val sb10 = configReceiveBufferSize(sb9)

      finagleServer = Some(sb10.build(service))



      globalSettings.foreach {
        _.onStart(this)
      }

      if (environment == Environment.Development) {
        info( """
                |********************************************************************
                |*** WARNING: Trinity is running in DEVELOPMENT mode.             ***
                |***                                ^^^^^^^^^^^                   ***
                |********************************************************************
              """.stripMargin)
      } else {
        info( """
                |********************************************************************
                |*** Trinity is running in Product mode.                          ***
                |********************************************************************
              """.stripMargin)
      }
    }
  }

  def stop()(implicit executor: ExecutionContext): Future[Unit] = synchronized {
    withDebugScope("stop") {
      require(finagleServer.isDefined)
      finagleServer.map {
        fs =>
          val result = fs.close().toScala
          globalSettings.foreach {
            _.onStop(this)
          }
          finagleServer = None
          result
      }.get
    }
  }

  def isStarted: Boolean = finagleServer.isDefined
}
