package org.sisioh.trinity.example

import org.sisioh.trinity.domain.io.http.Method._
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{Environment, Bootstrap}
import scala.concurrent.Future

object PlayLikeApplication2 extends App with Bootstrap {

  protected val environment: Environment.Value = Environment.Development

  class MainController {

    def helloWorld = SimpleAction {
      request =>
        throw new Exception
        Future.successful(Response().withContentAsString("Hello World!"))
    }

    def getUser = SimpleAction {
      request =>
        Future.successful(
          Response().withContentAsString("userId = " + request.routeParams("userId"))
        )
    }

    def getGroup(name: String) = SimpleAction {
      request =>
        Future.successful(Response().withContentAsString("name = " + name))
    }
  }

  val main = new MainController

  override protected val routingFilter = Some(RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> main.helloWorld,
        Get % "/user/:userId" -> main.getUser,
        Get %( """/group/(.*)""".r, Seq("name")) -> {
          request =>
            main.getGroup(request.routeParams("name"))(request)
        }
      )
  })

  await(start())


}
