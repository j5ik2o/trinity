package org.sisioh.trinity


import com.twitter.finagle.builder.{Server, ServerBuilder}


import com.twitter.finagle.http._
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.logging.config._
import com.twitter.logging.{FileHandler, LoggerFactory, Logger}
import java.lang.management.ManagementFactory
import java.net.InetSocketAddress
import com.twitter.finagle.tracing.{Tracer, NullTracer}
import com.twitter.conversions.storage._
import com.twitter.util.StorageUnit
import com.twitter.ostrich.admin._

//import com.twitter.ostrich.admin.Service

import com.twitter.ostrich.admin.{Service => OstrichService}
import com.twitter.ostrich.admin.RuntimeEnvironment
import com.twitter.ostrich.admin.AdminServiceFactory
import com.twitter.ostrich.admin.StatsFactory
import com.twitter.ostrich.admin.JsonStatsLoggerFactory
import com.twitter.ostrich.admin.TimeSeriesCollectorFactory
import com.twitter.ostrich.admin.ServiceTracker
import org.sisioh.scala.toolbox.LoggingEx

object TrinityServer {

  def apply(globalSetting: Option[GlobalSetting] = None) =
    new TrinityServer(globalSetting)

}

class TrinityServer(globalSetting: Option[GlobalSetting] = None)
  extends LoggingEx with OstrichService {

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
      httpPort = ConfigProvider.getInt("stats_port"),
      statsNodes = StatsFactory(
        reporters = JsonStatsLoggerFactory(serviceName = Some("finatra")) ::
          TimeSeriesCollectorFactory() :: Nil
      ) :: Nil
    )(runtimeEnv)
  }


  def shutdown {
    logger.info("shutting down")
    println("finatra process shutting down")
    System.exit(0)
  }

  def start {
    start(NullTracer, new RuntimeEnvironment(this))
  }

  def start(tracer: Tracer = NullTracer, runtimeEnv: RuntimeEnvironment = new RuntimeEnvironment(this)) {

    ServiceTracker.register(this)

    if (ConfigProvider.getBool("stats_enabled")) {
      initAdminService(runtimeEnv)
    }

    val appService = new ControllerService(controllers, globalSetting)
    val fileService = new FileService

    registerFilter(fileService)

    val port = ConfigProvider.getInt("port")

    val service: Service[FinagleRequest, FinagleResponse] = allFilters(appService)

    val http = Http().maxRequestSize(ConfigProvider.getInt("max_request_megabytes").megabyte)

    val codec = new RichHttp[FinagleRequest](http)

    val server: Server = ServerBuilder()
      .codec(codec)
      .bindTo(new InetSocketAddress(port))
      .tracer(tracer)
      .name(ConfigProvider.get("name"))
      .build(service)

    logger.info("process %s started on %s", pid, port)

    println("trinity process " + pid + " started on port: " + port.toString)
    //println("config args:")
    //ConfigProvider.printConfig()

  }
}


