package org.sisioh.trinity.test

import com.twitter.finagle.SimpleFilter
import com.twitter.finagle.http.{Response, Request}
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
import com.twitter.finagle.tracing.{NullTracer, Tracer}
import com.twitter.ostrich.admin.RuntimeEnvironment
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.controller.{Controller, ControllerRepositoryOnMemory, ControllerRepository}
import org.sisioh.trinity.domain.routing.RouteRepositoryOnMemory
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext

/**
 * モック用[[org.sisioh.trinity.application.TrinityApplication]]。
 *
 * @param config [[org.sisioh.trinity.domain.config.Config]]
 * @param routeRepository [[org.sisioh.trinity.domain.routing.RouteRepositoryOnMemory]]
 * @param controllerRepository
 * @param statsReceiver
 */
case class MockApplication
(config: Config = MockConfig(),
 routeRepository: RouteRepositoryOnMemory = new RouteRepositoryOnMemory,
 controllerRepository: ControllerRepository = new ControllerRepositoryOnMemory,
 statsReceiver: StatsReceiver = NullStatsReceiver)
  extends TrinityApplication {

  private implicit val entityIOContext = SyncEntityIOContext

  def registerController(controller: Controller) = synchronized {
    controllerRepository.store(controller)
    controller.routeRepository.foreach {
      e =>
        routeRepository.store(e).get
    }
  }

  def start() {}

  def start
  (tracer: Tracer = NullTracer,
   runtimeEnv: RuntimeEnvironment = new RuntimeEnvironment(this)): Unit = {}

  def shutdown() {}

  def registerFilter(filter: SimpleFilter[Request, Response]) {}
}
