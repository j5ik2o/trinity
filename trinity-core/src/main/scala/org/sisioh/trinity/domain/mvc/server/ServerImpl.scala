package org.sisioh.trinity.domain.mvc.server

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.builder.{Server => FinagleServer}
import com.twitter.finagle.http.Http
import com.twitter.finagle.http.RichHttp
import com.twitter.finagle.http.{Request => FinagleRequest}
import com.twitter.finagle.http.{Response => FinagleResponse}
import com.twitter.finagle.tracing.{NullTracer, Tracer}
import com.twitter.finagle.{Filter => FinagleFilter, Service}
import com.twitter.ostrich.admin._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.io.FinagleToIOFilter
import org.sisioh.trinity.domain.mvc.GatewayFilter
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.action.ActionExecuteService
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.{GlobalSettings, Filter}
import org.sisioh.trinity.infrastructure.util.DurationConverters._
import org.sisioh.trinity.infrastructure.util.FutureConverters._
import scala.Some
import scala.collection.mutable.ListBuffer
import scala.concurrent._

private[mvc]
class ServerImpl
(serverConfig: ServerConfig,
 actionOpt: Option[Action[Request, Response]],
 filterOpt: Option[Filter[Request, Response, Request, Response]],
 globalSettingsOpt: Option[GlobalSettings[Request, Response]])
(implicit executor: ExecutionContext)
  extends Server with LoggingEx {

  implicit val ctx = SyncEntityIOContext

  private var finagleServerOpt: Option[FinagleServer] = None

  private val finagleFilterBuffers = new ListBuffer[FinagleFilter[Request, Response, Request, Response]]()

  protected def createTracer: Tracer = NullTracer

  protected def createRuntimeEnviroment: RuntimeEnvironment = new RuntimeEnvironment(this)

  filterOpt.foreach(registerFilter)

  def registerFilters(filters: Seq[Filter[Request, Response, Request, Response]])(implicit executor: ExecutionContext) {
    registerFinagleFilters(filters.map {
      Filter toFinagleFilter _
    })
  }

  def registerFilter(filter: Filter[Request, Response, Request, Response])(implicit executor: ExecutionContext) {
    registerFinagleFilter(Filter.toFinagleFilter(filter))
  }

  protected def registerFinagleFilters(filters: Seq[FinagleFilter[Request, Response, Request, Response]]) {
    finagleFilterBuffers.appendAll(filters)
  }

  protected def registerFinagleFilter(filter: FinagleFilter[Request, Response, Request, Response]) {
    finagleFilterBuffers.append(filter)
  }

  protected def applyFinagleFilters(baseService: Service[Request, Response]) = {
    finagleFilterBuffers.foldRight(baseService) {
      (b, a) =>
        b andThen a
    }
  }

  private def createAdminService(runtimeEnv: RuntimeEnvironment) = withDebugScope("createAdminService") {
    AdminServiceFactory(
      httpPort = serverConfig.statsPort.getOrElse(9990),
      statsNodes = StatsFactory(
        reporters = JsonStatsLoggerFactory(serviceName = Some("trinity")) ::
          TimeSeriesCollectorFactory() :: Nil
      ) :: Nil
    )(runtimeEnv)
  }


  protected def createCodec = {
    import com.twitter.conversions.storage._
    val http = Http()
    serverConfig.maxRequestSizeOpt.foreach {
      v =>
        http.maxRequestSize(v.megabytes)
    }
    serverConfig.maxResponseSizeOpt.foreach {
      v =>
        http.maxResponseSize(v.megabytes)
    }
    RichHttp[FinagleRequest](http)
  }

  def start()(implicit executor: ExecutionContext): Future[Unit] = future {
    require(finagleServerOpt.isEmpty)
    if (serverConfig.statsEnabled) {
      createAdminService(createRuntimeEnviroment)
    }
    val actionExecuteService = ActionExecuteService(globalSettingsOpt)
    val service: Service[FinagleRequest, FinagleResponse] =
      FinagleToIOFilter() andThen
        GatewayFilter(actionOpt) andThen
        applyFinagleFilters(actionExecuteService)

    finagleServerOpt = Some(ServerBuilder()
      .codec(createCodec)
      .bindTo(serverConfig.bindAddressOpt.getOrElse(Server.defaultBindAddress))
      .tracer(createTracer)
      .name(serverConfig.nameOpt.getOrElse(Server.defaultName))
      .build(service))

    globalSettingsOpt.foreach {
      globalSettings =>
        globalSettings.onStart(this)
    }
  }

  def stop()(implicit executor: ExecutionContext): Future[Unit] =  {
    require(finagleServerOpt.isDefined)
    finagleServerOpt.map {
      finagleServer =>
        val result = finagleServer.close().toScala
        globalSettingsOpt.foreach {
          globalSettings =>
            globalSettings.onStop(this)
        }
        finagleServerOpt = None
        result
    }.get
  }

  def isStarted: Boolean = finagleServerOpt.isDefined
}
