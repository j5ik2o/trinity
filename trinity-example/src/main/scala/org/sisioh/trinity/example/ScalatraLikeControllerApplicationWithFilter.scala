package org.sisioh.trinity.example

import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Request, Response}
import org.sisioh.trinity.domain.mvc.{BootstrapWithScalatraLikeSupport, Environment}
import scala.concurrent.Future
import org.sisioh.trinity.domain.mvc.filter.SimpleFilter

object ScalatraLikeControllerApplicationWithFilter
  extends App with BootstrapWithScalatraLikeSupport {

  get("/hello") {
    request =>
      responseBuilder.withTextPlain("Hello World!!").toFuture
  }

  get("/user/:userId") {
    request =>
      responseBuilder.withTextPlain("userId = " + request.routeParams("userId")).toFuture
  }

  get( """/group/(.*)""".r, Seq("name")) {
    request =>
      responseBuilder.withTextPlain("name = " + request.routeParams("name")).toFuture
  }

  server.registerFilter(new SimpleFilter[Request, Response] {
    def apply(requestIn: Request, action: Action[Request, Response]): Future[Response] = {
      if ("shared secret" == requestIn.authorization) {
        action(requestIn)
      } else {
        Future.failed(new IllegalArgumentException())
      }
    }
  })

  startWithAwait()

}
