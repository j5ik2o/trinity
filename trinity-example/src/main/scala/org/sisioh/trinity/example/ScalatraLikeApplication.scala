package org.sisioh.trinity.example

import org.sisioh.trinity.domain.mvc.controller.ScalatraLikeController
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{Environment, Bootstrap}
import scala.concurrent.Future

object ScalatraLikeApplication extends App with ScalatraLikeController with Bootstrap {

  protected val environment = Environment.Development

  override protected val routingFilterOpt: Option[RoutingFilter] =
    Some(RoutingFilter.routeControllers(Seq(this)))

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

  await(start())
}
