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
package org.sisioh

/**
 * Represents package object for Trinity.
 */
package object trinity {

  val Environment = org.sisioh.trinity.domain.mvc.Environment
  val Methods = org.sisioh.trinity.domain.io.http.Methods
  val SimpleAction = org.sisioh.trinity.domain.mvc.action.SimpleAction
  val RouteDsl = org.sisioh.trinity.domain.mvc.routing.RouteDsl

  type Application = org.sisioh.trinity.domain.mvc.application.Application
  type ConsoleApplication = org.sisioh.trinity.domain.mvc.application.ConsoleApplication

  type RoutingFilter = org.sisioh.trinity.domain.mvc.routing.RoutingFilter
  val RoutingFilter = org.sisioh.trinity.domain.mvc.routing.RoutingFilter

  type Bootstrap = org.sisioh.trinity.domain.mvc.application.Bootstrap

  type ControllerSupport = org.sisioh.trinity.domain.mvc.controller.ControllerSupport
  type ScalatraLikeControllerSupport = org.sisioh.trinity.domain.mvc.controller.ScalatraLikeControllerSupport

  type ConsoleApplicationSupport = org.sisioh.trinity.domain.mvc.application.ConsoleApplicationSupport
  type ScalatraLikeApplicationSupport = org.sisioh.trinity.domain.mvc.application.ScalatraLikeApplicationSupport
  type SmallApplicationSupport = org.sisioh.trinity.domain.mvc.application.SmallApplicationSupport

  type ResponseBuilder = org.sisioh.trinity.domain.mvc.http.ResponseBuilder
  val ResponseBuilder = org.sisioh.trinity.domain.mvc.http.ResponseBuilder

  type Request = org.sisioh.trinity.domain.mvc.http.Request
  type Response = org.sisioh.trinity.domain.mvc.http.Response

  type Action[Request, Response] = org.sisioh.trinity.domain.mvc.action.Action[Request, Response]
  type SimpleFilter[Request, Response] = org.sisioh.trinity.domain.mvc.filter.SimpleFilter[Request, Response]

}
