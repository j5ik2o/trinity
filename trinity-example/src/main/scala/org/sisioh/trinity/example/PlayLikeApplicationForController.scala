package org.sisioh.trinity.example

import org.sisioh.trinity.domain.io.http.Method._
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.controller.ControllerSupport
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{Environment, Bootstrap}

object PlayLikeApplicationForController extends App with Bootstrap {

  protected val environment = Environment.Development

  case class MainController() extends ControllerSupport {

    def helloWorld = SimpleAction {
      request =>
        responseBuilder.withTextPlain("Hello World!!").toFuture
    }

    def getUser = SimpleAction {
      request =>
        responseBuilder.withTextPlain("userId = " + request.routeParams("userId")).toFuture
    }

    def getGroup(name: String) = SimpleAction {
      request =>
        responseBuilder.withTextPlain("name = " + name).toFuture
    }
  }

  val mainController = MainController()

  override protected val routingFilter = Some(RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> mainController.helloWorld,
        Get % "/user/:userId" -> mainController.getUser,
        Get % ("""/group/(.*)""".r -> Seq("name")) -> {
          request =>
            mainController.getGroup(request.routeParams("name"))(request)
        }
      )
  })

  startWithAwait()

}
