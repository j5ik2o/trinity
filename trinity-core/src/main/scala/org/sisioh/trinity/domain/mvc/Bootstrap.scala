package org.sisioh.trinity.domain.mvc

import java.util.concurrent.Executors
import org.sisioh.trinity.domain.io.transport.codec.http.Method
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await, ExecutionContext}

object Bootstrap extends App with Controller {

  implicit val executor = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  def helloWorld = SimpleAction {
    request =>
      Future.successful(Response().withContentAsString("Hello World!"))
  }

  @volatile private var counter = 0

  def getCounter = SimpleAction {
    request =>
      counter += 1
      Future.successful(Response().withContentAsString(counter.toString))
  }

  def printText(text: String) = SimpleAction {
    request =>
      Future.successful(Response().withContentAsString("text = " + text))
  }

  val routingFilter = RoutingFilter {
    Seq(
      Method.Get % "/hello" % this % helloWorld,
      Method.Get % "/counter" % this % getCounter,
      Method.Get % "/print/:text" % this % {
        request =>
          printText(request.routeParams("text"))(request)
      }
    )
  }

  val server = Server(ServerConfigLoader.load, filterOpt = Some(routingFilter))

  Await.result(server.start(), Duration.Inf)

}
