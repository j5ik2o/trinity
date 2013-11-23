package org.sisioh.trinity.view.scalate

import org.sisioh.trinity.domain.io.http.Method._
import org.sisioh.trinity.domain.mvc.Environment
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.controller.ControllerSupport
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter

object ScalateRendererApplication extends App with BootstrapWithScalateSupport {

  import Implicits._

  override protected val applicationId = "trinity-view-scalate"

  case class MainController() extends ControllerSupport {

    def helloWorld = SimpleAction {
      request =>
        responseBuilder.
          withScalate("helloWorld.mustache", Map("message" -> "hello World!")).toFuture
    }

  }

  val mainController = MainController()

  override protected val routingFilter = Some(RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> mainController.helloWorld
      )
  })

  startWithAwait()

}
