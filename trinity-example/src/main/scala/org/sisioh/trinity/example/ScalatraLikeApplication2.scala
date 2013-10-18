package org.sisioh.trinity.example

import org.sisioh.trinity.domain.mvc.controller.ScalatraLikeController
import org.sisioh.trinity.domain.mvc.http.{Request, Response}
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{SimpleFilter, Environment, Bootstrap}
import scala.concurrent.Future
import org.sisioh.trinity.domain.mvc.action.Action

object ScalatraLikeApplication2 extends App with ScalatraLikeController with Bootstrap {

  server.registerFilter(new SimpleFilter[Request, Response] {
    def apply(requestIn: Request, action: Action[Request, Response]): Future[Response] = {
      if ("shared secret" == requestIn.authorization) {
        action(requestIn)
      } else {
        Future.failed(new IllegalArgumentException())
      }
    }
  })

  protected val environment = Environment.Development

  get("/hello") {
    request =>
      Future.successful(Response().withContentAsString("Hello World!"))
  }

  get("/user/:userId") {
    request =>
      Future.successful(
        Response().withContentAsString("userId = " + request.routeParams("userId"))
      )
  }

  get( """/group/(.*)""".r, Seq("name")) {
    request =>
      Future.successful(
        Response().withContentAsString("name = " + request.routeParams("name"))
      )
  }

  override protected val routingFilterOpt =
    Some(RoutingFilter.createForControllers(Seq(this)))

  await(start())
}
