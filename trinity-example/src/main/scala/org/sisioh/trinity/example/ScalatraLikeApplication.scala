package org.sisioh.trinity.example

import java.util.concurrent.Executors
import org.sisioh.trinity.domain.mvc.controller.ScalatraLikeController
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{SinatraPathPatternParser, PathPatternParser}
import org.sisioh.trinity.domain.mvc.server.{ServerConfigLoader, Server}
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future, Await}
import org.sisioh.trinity.domain.mvc.Environment

object ScalatraLikeApplication extends App with ScalatraLikeController {

  implicit val executor = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  implicit val pathPatternParser: PathPatternParser = SinatraPathPatternParser()

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

  get("""/(abc.*)""".r, List("path")) {
    request =>
      Future.successful(Response().withContentAsString("path = " + request.routeParams("path")))
  }

  val routingFilter = RoutingFilter.routeControllers(Seq(this))

  val server = Server(ServerConfigLoader.load(Environment.Development), filterOpt = Some(routingFilter))
  Await.result(server.start(), Duration.Inf)

}
