package org.sisioh.trinity.example

import org.sisioh.trinity.domain.mvc.controller.ScalatraLikeController
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{Environment, Bootstrap}
import scala.concurrent.Future

object ScalatraLikeApplication extends App with ScalatraLikeController with Bootstrap {

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

  get("""/group/(.*)""".r, Seq("name")) {
    request =>
      Future.successful(
        Response().withContentAsString("name = " + request.routeParams("name"))
      )
  }

  override protected val routingFilterOpt =
    Some(RoutingFilter.createForControllers(Seq(this)))

  await(start())
}
