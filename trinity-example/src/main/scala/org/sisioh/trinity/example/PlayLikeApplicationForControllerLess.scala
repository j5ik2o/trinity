package org.sisioh.trinity.example

import org.sisioh.trinity._
import org.sisioh.trinity.Methods._
import org.sisioh.trinity.RouteDsl._

object PlayLikeApplicationForControllerLess extends ConsoleApplication  with ControllerSupport {

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

  override protected lazy val routingFilter = Some(RoutingFilter.createForActions {
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
