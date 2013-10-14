package org.sisioh.trinity.example

import org.sisioh.trinity.domain.io.transport.codec.http.Method._
import org.sisioh.trinity.domain.mvc.{Environment, Bootstrap}
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.controller.Controller
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.stats.Stats
import scala.concurrent.Future

object PlayLikeApplication extends App with Controller with Bootstrap {

  protected val environment = Environment.Development

  def helloWorld = SimpleAction {
    request =>
      Stats.timeFutureNanos("hello") {
        Future.successful(Response().withContentAsString("Hello World!"))
      }
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


  override protected val routingFilterOpt: Option[RoutingFilter] = Some(RoutingFilter.routes {
    implicit pathPatternParser =>
      Seq(
        // 簡単 その1
        Get % "/hello" -> helloWorld,
        // 簡単 その2
        Get % "/counter" -> getCounter,
        Get % "/user/:userId" -> getUser,
        // メソッドの引数にパラメータを渡したい場合
        Get % "/print/:text" -> {
          request =>
            printText(request.routeParams("text"))(request)
        },
        // 正規表現を指定したい場合
        Get % ("""/(abc.*)""".r -> Seq("path")) -> getPath
      )
  })


  await(start())

}
