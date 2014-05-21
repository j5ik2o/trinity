package org.sisioh.trinity.example

import org.sisioh.trinity._
import scala.concurrent.Future
import org.sisioh.trinity.domain.mvc.resource.FileResourceReadFilter
import java.io.File
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter

object ScalatraLikeControllerApplicationWithFilter
  extends ConsoleApplication with ScalatraLikeControllerSupport {

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
  override protected lazy val routingFilter =
    Some(RoutingFilter.createFromControllers(this))

  server.registerFilter(new FileResourceReadFilter(environment, new File(".")))

  startWithAwait()

}
