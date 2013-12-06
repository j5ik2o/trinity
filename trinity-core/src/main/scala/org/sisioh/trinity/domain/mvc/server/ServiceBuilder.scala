/*
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
package org.sisioh.trinity.domain.mvc.server

import com.twitter.finagle.http.{Response => FinagleResponse, Request => FinagleRequest}
import com.twitter.finagle.{Filter => FinagleFilter, Service}
import org.sisioh.trinity.domain.io.FinagleToIOFilter
import org.sisioh.trinity.domain.mvc._
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.action.ActionExecuteService
import org.sisioh.trinity.domain.mvc.action.ExceptionHandleFilter
import org.sisioh.trinity.domain.mvc.filter.{RequestDumpFilter, GatewayFilter, Filter}
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext

/**
 * Finagleサービスを作成するためのビルダー責務。
 */
trait ServiceBuilder {

  private val finagleFilterBuffers = new ListBuffer[FinagleFilter[Request, Response, Request, Response]]()

  val globalSettings: Option[GlobalSettings[Request, Response]]

  protected def applyFinagleFilters(baseService: Service[Request, Response]) = {
    finagleFilterBuffers.foldRight(baseService) {
      (b, a) =>
        b andThen a
    }
  }

  protected def unregisterAllFinagleFilters() = {
    finagleFilterBuffers.clear()
  }

  protected def registerFinagleFilters(filters: Seq[FinagleFilter[Request, Response, Request, Response]]) {
    finagleFilterBuffers.appendAll(filters)
  }

  protected def registerFinagleFilter(filter: FinagleFilter[Request, Response, Request, Response]) {
    finagleFilterBuffers.append(filter)
  }

  def unregisterAllFilters() = {
    unregisterAllFinagleFilters()
  }

  def registerFilters(filters: Seq[Filter[Request, Response, Request, Response]])(implicit executor: ExecutionContext) {
    registerFinagleFilters(filters.map {
      Filter toFinagleFilter _
    })
  }

  def registerFilter(filter: Filter[Request, Response, Request, Response])(implicit executor: ExecutionContext) {
    registerFinagleFilter(Filter.toFinagleFilter(filter))
  }

  protected def buildService(environment: Environment.Value, action: Option[Action[Request, Response]] = None)(implicit executor: ExecutionContext) = {
    val actionExecuteService = ActionExecuteService(globalSettings)
    def applyFilter() = {
      if (environment == Environment.Development)
        Filter.toFinagleFilter(RequestDumpFilter()) andThen
          Filter.toFinagleFilter(ExceptionHandleFilter(globalSettings)) andThen
          applyFinagleFilters(actionExecuteService)
      else
        Filter.toFinagleFilter(ExceptionHandleFilter(globalSettings)) andThen
          applyFinagleFilters(actionExecuteService)
    }
    val service: Service[FinagleRequest, FinagleResponse] =
      FinagleToIOFilter() andThen
        GatewayFilter(action) andThen applyFilter

    service
  }

}
