package org.sisioh

package object trinity {

  val Environment = org.sisioh.trinity.domain.mvc.Environment
  val Methods = org.sisioh.trinity.domain.io.http.Methods
  val SimpleAction = org.sisioh.trinity.domain.mvc.action.SimpleAction
  val RouteDsl = org.sisioh.trinity.domain.mvc.routing.RouteDsl

  type Application = org.sisioh.trinity.domain.mvc.application.Application
  type ConsoleApplication = org.sisioh.trinity.domain.mvc.application.ConsoleApplication

  type RoutingFilter = org.sisioh.trinity.domain.mvc.routing.RoutingFilter
  val RoutingFilter = org.sisioh.trinity.domain.mvc.routing.RoutingFilter

  type ControllerSupport = org.sisioh.trinity.domain.mvc.controller.ControllerSupport
  type ConsoleApplicationSupport = org.sisioh.trinity.domain.mvc.application.ConsoleApplicationSupport

  type ScalatraLikeControllerSupport = org.sisioh.trinity.domain.mvc.controller.ScalatraLikeControllerSupport

  type ResponseBuilder = org.sisioh.trinity.domain.mvc.http.ResponseBuilder
  val ResponseBuilder = org.sisioh.trinity.domain.mvc.http.ResponseBuilder


  type Request = org.sisioh.trinity.domain.mvc.http.Request
  type Response = org.sisioh.trinity.domain.mvc.http.Response

  type Action[Request, Response] = org.sisioh.trinity.domain.mvc.action.Action[Request, Response]
  type SimpleFilter[Request, Response] = org.sisioh.trinity.domain.mvc.filter.SimpleFilter[Request, Response]

}
