package org.sisioh.trinity.example

import org.sisioh.trinity.domain.io.http.Method._
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.http.{ResponseSupport, ResponseBuilder}
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{BootstrapWithResponseSupport, Environment, Bootstrap}

object PlayLikeApplicationForControllerLess extends App with BootstrapWithResponseSupport {

  protected val environment = Environment.Development

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

  override protected val routingFilter = Some(RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> helloWorld,
        Get % "/user/:userId" -> getUser,
        Get % ("""/group/(.*)""".r -> Seq("name")) -> {
          request =>
            getGroup(request.routeParams("name"))(request)
        }
      )
  })

  startWithAwait()

}
