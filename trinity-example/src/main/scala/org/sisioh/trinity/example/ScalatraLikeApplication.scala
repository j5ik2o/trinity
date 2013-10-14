package org.sisioh.trinity.example

import org.sisioh.trinity.domain.mvc.controller.ScalatraLikeController
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import scala.concurrent.Future
import org.sisioh.trinity.domain.mvc.{Environment, Bootstrap}

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
      Future.successful(Response().withContentAsString("userId = " + request.routeParams("userId")))
  }

  @volatile private var counter = 0

  get("/counter") {
    request =>
      counter += 1
      Future.successful(Response().withContentAsString(counter.toString))
  }

  get("/print/:text") {
    request =>
      Future.successful(Response().withContentAsString("text = " + request.routeParams("text")))
  }

  get( """/(abc.*)""".r, List("path")) {
    request =>
      Future.successful(Response().withContentAsString("path = " + request.routeParams("path")))
  }

  await(start())
}
