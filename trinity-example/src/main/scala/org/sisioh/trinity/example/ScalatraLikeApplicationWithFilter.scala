package org.sisioh.trinity.example

import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.controller.ScalatraLikeSupport
import org.sisioh.trinity.domain.mvc.http.{ResponseBuilder, Request, Response}
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{SimpleFilter, Environment, Bootstrap}
import scala.concurrent.Future

object ScalatraLikeApplicationWithFilter extends App with ScalatraLikeSupport with Bootstrap {

  protected val environment = Environment.Development

  get("/hello") {
    request =>
      ResponseBuilder().withTextPlain("Hello World!!").toFuture
  }

  get("/user/:userId") {
    request =>
      ResponseBuilder().withTextPlain("userId = " + request.routeParams("userId")).toFuture
  }

  get( """/group/(.*)""".r, Seq("name")) {
    request =>
      ResponseBuilder().withTextPlain("name = " + request.routeParams("name")).toFuture
  }

  override protected val routingFilter =
    Some(RoutingFilter.createForControllers(Seq(this)))

  server.registerFilter(new SimpleFilter[Request, Response] {
    def apply(requestIn: Request, action: Action[Request, Response]): Future[Response] = {
      if ("shared secret" == requestIn.authorization) {
        action(requestIn)
      } else {
        Future.failed(new IllegalArgumentException())
      }
    }
  })

  await(start())
}
