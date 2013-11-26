package org.sisioh.trinity.view.scalate

import org.sisioh.trinity._
import org.sisioh.trinity.Methods._
import org.sisioh.trinity.RouteDsl._

object ScalateRendererApplication
  extends ConsoleApplication with ConsoleApplicationSupport
  with ScalateApplicationSupport {

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

  override protected lazy val routingFilter = Some(RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> mainController.helloWorld
      )
  })

  startWithAwait()

}
