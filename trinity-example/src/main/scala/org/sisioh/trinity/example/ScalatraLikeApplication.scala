package org.sisioh.trinity.example

import org.json4s._
import org.sisioh.trinity.domain.mvc.controller.ScalatraLikeSupport
import org.sisioh.trinity.domain.mvc.http.{JSON4SRenderer, ResponseBuilder}
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{Environment, Bootstrap}

object ScalatraLikeApplication extends App with ScalatraLikeSupport with Bootstrap {

  protected val environment = Environment.Development

  get("/hello") {
    request =>
      ResponseBuilder().withTextPlain("Hello World!!").toFuture
  }

  get("/json") {
    request =>
      val jValue = JObject(
        JField("name", JString("value"))
      )
      ResponseBuilder().withRenderer(JSON4SRenderer(jValue)).toFuture
  }

  get("/user/:userId") {
    request =>
      ResponseBuilder().withTextPlain("userId = " + request.routeParams("userId")).toFuture
  }

  get( """/group/(.*)""".r, Seq("name")) {
    request =>
      ResponseBuilder().withTextPlain("name = " + request.routeParams("name")).toFuture
  }

  override protected val routingFilter = Some(RoutingFilter.createForControllers(this))

  await(start())

}
