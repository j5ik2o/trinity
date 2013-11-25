package org.sisioh.trinity.domain.mvc.server

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.builder.{Server => FinagleServer}
import com.twitter.finagle.http.Http
import com.twitter.finagle.http.RichHttp
import com.twitter.finagle.http.{Request => FinagleRequest}
import com.twitter.finagle.tracing.{NullTracer, Tracer}
import com.twitter.ostrich.admin._
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.{Environment, GlobalSettings, Filter}
import org.sisioh.trinity.infrastructure.util.FutureConverters._
import scala.concurrent._

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

  filter.foreach(registerFilter)

  private def createAdminService(runtimeEnv: RuntimeEnvironment) = withDebugScope("createAdminService") {
    AdminServiceFactory(
      httpPort = serverConfig.statsPort.getOrElse(9990),
      statsNodes = StatsFactory(
        reporters = JsonStatsLoggerFactory(serviceName = serverConfig.name) ::
          TimeSeriesCollectorFactory() :: Nil
      ) :: Nil
    )(runtimeEnv)
  }


  protected def createCodec = {
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

  def start(environment: Environment.Value = Environment.Development)
           (implicit executor: ExecutionContext): Future[Unit] = future {
    withDebugScope("start") {
      if (environment == Environment.Development) {
        info( """
                |********************************************************************
                |*** WARNING: Trinity is running in DEVELOPMENT mode.             ***
                |***                               ^^^^^^^^^^^                    ***
                |********************************************************************
              """.stripMargin)
      }
      require(finagleServer.isEmpty)
      if (serverConfig.statsEnabled) {
        createAdminService(createRuntimeEnviroment)
      }

      val service = buildService(action)

      val bindAddress = serverConfig.bindAddress.getOrElse(Server.defaultBindAddress)
      scopedDebug(s"bindAddress = $bindAddress")
      val name = serverConfig.name.getOrElse(Server.defaultName)
      scopedDebug(s"name = $name")

      finagleServer = Some(
        ServerBuilder()
          .codec(createCodec)
          .bindTo(bindAddress)
          .tracer(createTracer)
          .name(name)
          .build(service)
      )

      globalSettings.foreach {
        _.onStart(this)
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
