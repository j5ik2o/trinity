/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
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
package org.sisioh.trinity.application

import com.twitter.conversions.storage._
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.http._
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
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
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.controller.{ControllerRepositoryOnMemory, GlobalSettings, ControllerService, Controller}
import org.sisioh.trinity.domain.resource.FileReadFilter
import org.sisioh.trinity.domain.routing.RouteRepositoryOnMemory
import org.sisioh.trinity.infrastructure.DurationUtil._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext

/**
 * [[org.sisioh.trinity.application.TrinityApplication]]のデフォルト実装。
 *
 * @param config [[org.sisioh.trinity.domain.config.Config]]
 * @param globalSetting
 */
private[application]
class TrinityApplicationImpl(val config: Config, globalSetting: Option[GlobalSettings] = None)
  extends TrinityApplication with LoggingEx with OstrichService {

  private var opened = false

  private var server: Server = _

  val statsReceiver: StatsReceiver = NullStatsReceiver

  val routeRepository = new RouteRepositoryOnMemory

  val controllerRepository = new ControllerRepositoryOnMemory

  private implicit val entityIOContext = SyncEntityIOContext

  private var filters: Seq[SimpleFilter[FinagleRequest, FinagleResponse]] = Seq.empty

  val pid = ManagementFactory.getRuntimeMXBean.getName.split('@').head

  def allFilters(baseService: Service[FinagleRequest, FinagleResponse]) = {
    filters.foldRight(baseService) {
      (b, a) =>
        b andThen a
    }
  }

  def registerController(controller: Controller): Unit = synchronized {
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


  def shutdown() = synchronized {
    if (opened) {
      Await.ready(server.close(), config.awaitDuration.toTwitter)
      info("shutting down")
      globalSetting.foreach(_.onStop(this))
      opened = false
    }
  }

  def start() {
    start(NullTracer, new RuntimeEnvironment(this))
  }

  def start
  (tracer: Tracer = NullTracer,
   runtimeEnv: RuntimeEnvironment = new RuntimeEnvironment(this)): Unit = synchronized{

    ServiceTracker.register(this)

    if (config.statsEnabled) {
      initAdminService(runtimeEnv)
    }

    val controllerService = new ControllerService(this, globalSetting)
    val fileReadFilter = new FileReadFilter(config)

    registerFilter(fileReadFilter)

    val port = config.applicationPort.getOrElse(7070)

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
        serverBuilder.hostConnectionMaxIdleTime(v.toTwitter)
    }

    server = serverBuilder
      .build(service)

    logger.info("process %s started on %s", pid, port)

    println("trinity process " + pid + " started on port: " + port.toString)
    println("config args:")
    println(config)

    globalSetting.foreach(_.onStart(this))

    opened = true

  }

}


