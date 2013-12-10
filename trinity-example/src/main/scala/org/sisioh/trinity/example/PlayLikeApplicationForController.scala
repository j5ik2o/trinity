package org.sisioh.trinity.example

import org.sisioh.trinity.Methods._
import org.sisioh.trinity.RouteDsl._
import org.sisioh.trinity._

object PlayLikeApplicationForController extends ConsoleApplication {

  override protected lazy val applicationId = "trinity-example"

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

  override protected lazy val routingFilter = Some(RoutingFilter.createForActions {
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
