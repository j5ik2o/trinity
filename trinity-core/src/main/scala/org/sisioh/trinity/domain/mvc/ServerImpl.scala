package org.sisioh.trinity.domain.mvc

import scala.concurrent._
import org.sisioh.trinity.domain.io.FinagleToIOFilter
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{Http, RichHttp}
import java.net.{InetSocketAddress, SocketAddress}
import org.sisioh.trinity.domain.mvc.routing.{Route, RouteRepository, RoutingFilter}
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request => FinagleRequest}
import com.twitter.finagle.http.{Response => FinagleResponse}
import com.twitter.finagle.builder.{Server => FinagleServer}
import org.sisioh.trinity.infrastructure.util.FutureConverters._
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext

class ServerImpl
(name: String = "trinity",
 endPointAddress: SocketAddress = new InetSocketAddress(8080),
 actionOpt: Option[Action[Request, Response]] = None,
 globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None)
(implicit executor: ExecutionContext) extends Server {

  implicit val ctx = SyncEntityIOContext
  private var server: FinagleServer = _
  private val routeRepository = RouteRepository.ofMemory
  private val controllerRepository = ControllerRepository.ofMemory
  private val routingFilter = RoutingFilter(routeRepository, controllerRepository, globalSettingsOpt)
  private val actionExecuteService = ActionExecuteService(globalSettingsOpt)

  def registerControllers(controllers: Seq[Controller[Request, Response]]): Unit = {
    controllerRepository.store(controllers)
  }

  def registerController(controller: Controller[Request, Response]): Unit = {
    controllerRepository.store(controller)
  }

  def registerRoutes(routes: Seq[Route[Request, Response]]): Unit = {
    routeRepository.store(routes)
  }

  def registerRoute(route: Route[Request, Response]): Unit = {
    routeRepository.store(route)
  }

  def start()(implicit executor: ExecutionContext): Future[Unit] = future {
    val service: Service[FinagleRequest, FinagleResponse] =
      FinagleToIOFilter() andThen
        GatewayFilter(actionOpt) andThen
        routingFilter andThen
        actionExecuteService

    server = ServerBuilder()
      .codec(RichHttp[FinagleRequest](Http()))
      .bindTo(endPointAddress)
      .name(name)
      .build(service)
  }

  def stop()(implicit executor: ExecutionContext): Future[Unit] =
    server.close().toScala

}
