package org.sisioh.trinity.example

import org.json4s._
import org.sisioh.trinity.domain.mvc.controller.ScalatraLikeControllerSupport
import org.sisioh.trinity.domain.mvc.http.{JSON4SRenderer, ResponseBuilder}
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{BootstrapWithScalatraLikeSupport, Environment, Bootstrap}

object ScalatraLikeControllerApplication
  extends App with BootstrapWithScalatraLikeSupport {

  protected val environment = Environment.Development

  get("/hello") {
    request =>
      responseBuilder.withTextPlain("Hello World!!").toFuture
  }

  get("/json") {
    request =>
      val jValue = JObject(
        JField("name", JString("value"))
      )
      responseBuilder.withJson(jValue).toFuture
  }

  get("/user/:userId") {
    request =>
      responseBuilder.withTextPlain("userId = " + request.routeParams("userId")).toFuture
  }

  get( """/group/(.*)""".r, Seq("name")) {
    request =>
      ResponseBuilder().withTextPlain("name = " + request.routeParams("name")).toFuture
  }

  startWithAwait()

}
