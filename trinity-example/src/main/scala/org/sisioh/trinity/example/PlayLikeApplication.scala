package org.sisioh.trinity.example

import java.util.concurrent.Executors
import org.sisioh.trinity.domain.io.transport.codec.http.Method
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.controller.Controller
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.{RoutingFilter}
import org.sisioh.trinity.domain.mvc.server.{ServerConfigLoader, Server}
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await, ExecutionContext}
import org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPattern

object PlayLikeApplication extends App with Controller {

  implicit val executor = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  def helloWorld = SimpleAction {
    request =>
      Future.successful(Response().withContentAsString("Hello World!"))
  }

  def getUser = SimpleAction {
    request =>
      Future.successful(Response().withContentAsString("userId = " + request.routeParams("userId")))
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

  def getPath = SimpleAction {
    request =>
      Future.successful(Response().withContentAsString("path = " + request.routeParams("path")))
  }

  val routingFilter = RoutingFilter.routes {
    implicit pathPatternParser =>
      Seq(
        // 簡単 その1
        Method.Get % "/hello" % this % helloWorld,
        // 簡単 その2
        Method.Get % "/counter" % this % getCounter,
        Method.Get % "/user/:userId" % this % getUser,
        // メソッドの引数にパラメータを渡したい場合
        Method.Get % "/print/:text" % this % {
          request =>
            printText(request.routeParams("text"))(request)
        },
        // 正規表現を指定したい場合
        Method.Get % PathPattern("""/(abc.*)""".r, List("path")) % this % getPath
      )
  }

  val server = Server(ServerConfigLoader.load, filterOpt = Some(routingFilter))

  Await.result(server.start(), Duration.Inf)

}
