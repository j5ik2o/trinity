package org.sisioh.trinity

import com.twitter.finagle.builder.{Server, ServerBuilder}


import com.twitter.finagle.http._
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.finagle.{Service, SimpleFilter}
import java.lang.management.ManagementFactory
import java.net.InetSocketAddress
import com.twitter.finagle.tracing.{Tracer, NullTracer}
import com.twitter.conversions.storage._
import com.twitter.ostrich.admin.{Service => OstrichService}
import com.twitter.ostrich.admin.RuntimeEnvironment
import com.twitter.ostrich.admin.AdminServiceFactory
import com.twitter.ostrich.admin.StatsFactory
import com.twitter.ostrich.admin.JsonStatsLoggerFactory
import com.twitter.ostrich.admin.TimeSeriesCollectorFactory
import com.twitter.ostrich.admin.ServiceTracker
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.infrastructure.DurationUtil
import com.twitter.util.Await

object TrinityServer {

  def apply(config: Config, globalSetting: Option[GlobalSetting] = None) =
    new TrinityServer(config, globalSetting)

}

class TrinityServer(val config: Config, globalSetting: Option[GlobalSetting] = None)
  extends LoggingEx with OstrichService {

  private var server: Server = _
  private val controllers = new Controllers
  private var filters: Seq[SimpleFilter[FinagleRequest, FinagleResponse]] = Seq.empty

  val pid = ManagementFactory.getRuntimeMXBean().getName().split('@').head

  def allFilters(baseService: Service[FinagleRequest, FinagleResponse]) = {
    filters.foldRight(baseService) {
      (b, a) =>
        b andThen a
    }
  }

  def registerController(app: Controller) {
    controllers.add(app)
  }

  def registerFilter(filter: SimpleFilter[FinagleRequest, FinagleResponse]) {
    filters = filters ++ Seq(filter)
  }

  private def initAdminService(runtimeEnv: RuntimeEnvironment) {
    AdminServiceFactory(
      httpPort = config.statsPort.get,
      statsNodes = StatsFactory(
        reporters = JsonStatsLoggerFactory(serviceName = Some("trinity")) ::
          TimeSeriesCollectorFactory() :: Nil
      ) :: Nil
    )(runtimeEnv)
  }


  def shutdown {
    Await.ready(server.close())
    info("shutting down")
    System.exit(0)
  }

  def start {
    start(NullTracer, new RuntimeEnvironment(this))
  }

  def start(tracer: Tracer = NullTracer, runtimeEnv: RuntimeEnvironment = new RuntimeEnvironment(this)) {

    ServiceTracker.register(this)

    if (config.statsEnabled) {
      initAdminService(runtimeEnv)
    }

    val appService = new ControllerService(controllers, globalSetting)
    val fileService = new FileService(config)

    registerFilter(fileService)

    val port = config.applicationPort.get

    val service: Service[FinagleRequest, FinagleResponse] = allFilters(appService)

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
    println(Config)

  }
}


