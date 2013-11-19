package org.sisioh.trinity.domain.mvc.server

import com.twitter.finagle.http.{Response => FinagleResponse, Request => FinagleRequest}
import com.twitter.finagle.{Filter => FinagleFilter, Service}
import org.sisioh.trinity.domain.io.FinagleToIOFilter
import org.sisioh.trinity.domain.mvc.action.ActionExecuteService
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.{GlobalSettings, Filter, GatewayFilter}
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext

trait ServiceBuilder {

  private val finagleFilterBuffers = new ListBuffer[FinagleFilter[Request, Response, Request, Response]]()

  protected val globalSettings: Option[GlobalSettings[Request, Response]]

  protected def applyFinagleFilters(baseService: Service[Request, Response]) = {
    finagleFilterBuffers.foldRight(baseService) {
      (b, a) =>
        b andThen a
    }
  }

  protected def registerFinagleFilters(filters: Seq[FinagleFilter[Request, Response, Request, Response]]) {
    finagleFilterBuffers.appendAll(filters)
  }

  protected def registerFinagleFilter(filter: FinagleFilter[Request, Response, Request, Response]) {
    finagleFilterBuffers.append(filter)
  }

  def registerFilters(filters: Seq[Filter[Request, Response, Request, Response]])(implicit executor: ExecutionContext) {
    registerFinagleFilters(filters.map {
      Filter toFinagleFilter _
    })
  }

  def registerFilter(filter: Filter[Request, Response, Request, Response])(implicit executor: ExecutionContext) {
    registerFinagleFilter(Filter.toFinagleFilter(filter))
  }

  def buildService(implicit executor: ExecutionContext) = {
    val actionExecuteService = ActionExecuteService(globalSettings)
    val service: Service[FinagleRequest, FinagleResponse] =
      FinagleToIOFilter() andThen
        GatewayFilter(None) andThen applyFinagleFilters(actionExecuteService)
    service
  }

}
