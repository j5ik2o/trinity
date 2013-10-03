package org.sisioh.trinity.domain.mvc

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.builder.{Server => FinagleServer}
import com.twitter.finagle.http.{Http, RichHttp}
import com.twitter.finagle.http.{Request => FinagleRequest}
import com.twitter.finagle.http.{Response => FinagleResponse}
import com.twitter.finagle.{Filter => FinagleFilter, Service}
import java.net.SocketAddress
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import org.sisioh.trinity.domain.io.FinagleToIOFilter
import org.sisioh.trinity.infrastructure.util.FutureConverters._
import scala.collection.mutable.ListBuffer
import scala.concurrent._

class ServerImpl
(bindAddress: SocketAddress,
 name: String = "trinity",
 actionOpt: Option[Action[Request, Response]] = None,
 globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None)
 extends Server {

  implicit val ctx = SyncEntityIOContext
  private var server: FinagleServer = _

  private val finagleFilterBuffers = new ListBuffer[FinagleFilter[Request, Response, Request, Response]]()

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

  def start()(implicit executor: ExecutionContext): Future[Unit] = future {
    val actionExecuteService = ActionExecuteService(globalSettingsOpt)
    val service: Service[FinagleRequest, FinagleResponse] =
      FinagleToIOFilter() andThen
        GatewayFilter(actionOpt) andThen
        applyFinagleFilters(actionExecuteService)

    server = ServerBuilder()
      .codec(RichHttp[FinagleRequest](Http()))
      .bindTo(bindAddress)
      .name(name)
      .build(service)
  }

  def stop()(implicit executor: ExecutionContext): Future[Unit] =
    server.close().toScala

}
