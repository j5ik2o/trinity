package org.sisioh.trinity.application

import com.twitter.conversions.storage._
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.http.RichHttp
import com.twitter.finagle.http._
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.finagle.tracing.{Tracer, NullTracer}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.ostrich.admin.AdminServiceFactory
import com.twitter.ostrich.admin.JsonStatsLoggerFactory
import com.twitter.ostrich.admin.RuntimeEnvironment
import com.twitter.ostrich.admin.ServiceTracker
import com.twitter.ostrich.admin.StatsFactory
import com.twitter.ostrich.admin.TimeSeriesCollectorFactory
import com.twitter.ostrich.admin.{Service => OstrichService}
import com.twitter.util.Await
import java.lang.management.ManagementFactory
import java.net.InetSocketAddress
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain._
import org.sisioh.trinity.infrastructure.DurationUtil
import scala.Some
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
import org.sisioh.trinity.domain.routing.RouteRepositoryOnMemory
import org.sisioh.trinity.domain.resource.FileReadFilter
import org.sisioh.trinity.domain.controller.{ControllerRepositoryOnMemory, GlobalSettings, ControllerService, Controller}
import org.sisioh.trinity.domain.config.Config

private[application]
class TrinityApplicationImpl(val config: Config, globalSetting: Option[GlobalSettings] = None)
  extends TrinityApplication with LoggingEx with OstrichService {

  private var server: Server = _

  val statsReceiver: StatsReceiver = NullStatsReceiver

  val routeRepository = new RouteRepositoryOnMemory

  val controllerRepository = new ControllerRepositoryOnMemory

  private var filters: Seq[SimpleFilter[FinagleRequest, FinagleResponse]] = Seq.empty

  val pid = ManagementFactory.getRuntimeMXBean.getName.split('@').head

  def allFilters(baseService: Service[FinagleRequest, FinagleResponse]) = {
    filters.foldRight(baseService) {
      (b, a) =>
        b andThen a
    }
  }

  def registerController(controller: Controller) {
    controllerRepository.store(controller)
    controller.routeRepository.foreach {
      e =>
        routeRepository.store(e).get
    }
  }

  def registerFilter(filter: SimpleFilter[FinagleRequest, FinagleResponse]) {
    filters = filters ++ Seq(filter)
  }

  private def initAdminService(runtimeEnv: RuntimeEnvironment) {
    AdminServiceFactory(
      httpPort = config.statsPort.getOrElse(9990),
      statsNodes = StatsFactory(
        reporters = JsonStatsLoggerFactory(serviceName = Some("trinity")) ::
          TimeSeriesCollectorFactory() :: Nil
      ) :: Nil
    )(runtimeEnv)
  }


  def shutdown() {
    Await.ready(server.close())
    info("shutting down")
    globalSetting.foreach(_.onStop(this))
  }

  def start() {
    start(NullTracer, new RuntimeEnvironment(this))
  }

  def start(tracer: Tracer = NullTracer, runtimeEnv: RuntimeEnvironment = new RuntimeEnvironment(this)) {

    ServiceTracker.register(this)

    if (config.statsEnabled) {
      initAdminService(runtimeEnv)
    }

    val controllerService = new ControllerService(this, globalSetting)
    val fileReadFilter = new FileReadFilter(config)

    registerFilter(fileReadFilter)

    val port = config.applicationPort.get

    val service: Service[FinagleRequest, FinagleResponse] = allFilters(controllerService)

    val http = {
      val result = Http()
      config.maxRequestSize.foreach {
        v =>
          result.maxRequestSize(v.megabytes)
      }
      config.maxResponseSize.foreach {
        v =>
          result.maxResponseSize(v.megabytes)
      }
      result
    }

    val codec = new RichHttp[FinagleRequest](http)

    val serverBuilder = ServerBuilder()
      .codec(codec)
      .bindTo(new InetSocketAddress(port))
      .tracer(tracer)
      .name(config.applicationName)

    config.maxConcurrentRequests.foreach {
      v =>
        serverBuilder.maxConcurrentRequests(v)
    }
    config.hostConnectionMaxIdleTime.foreach {
      v =>
        import DurationUtil._
        serverBuilder.hostConnectionMaxIdleTime(v.toTwitter)
    }

    server = serverBuilder
      .build(service)

    logger.info("process %s started on %s", pid, port)

    println("trinity process " + pid + " started on port: " + port.toString)
    println("config args:")
    println(config)

    globalSetting.foreach(_.onStart(this))

  }

}


