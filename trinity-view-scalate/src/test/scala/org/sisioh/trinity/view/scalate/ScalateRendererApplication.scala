package org.sisioh.trinity.view.scalate

import java.io.File
import org.sisioh.trinity.domain.io.http.Method._
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.http.ResponseBuilder
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{Environment, Bootstrap}

object ScalateRendererApplication extends App with Bootstrap {

  protected val environment: Environment.Value = Environment.Development

  implicit val scalateContext = ScalateEngineContext(
    environment,
    new File("/tmp/work"),
    "trinity-view-scalate/src/test/resources",
    "/"
  )

  class MainController {

    def helloWorld = SimpleAction {
      request =>
        ResponseBuilder().withRenderer(ScalateRenderer("helloWorld.mustache", Map("message" -> "hoge"))).toFuture
    }

  }

  val main = new MainController

  override protected val routingFilter = Some(RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> main.helloWorld
      )
  })

  await(start())


}
