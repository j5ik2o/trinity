package org.sisioh.trinity.example

import org.sisioh.trinity.domain.io.http.Method._
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.controller.Controller
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.{Environment, Bootstrap}
import scala.concurrent.Future

object PlayLikeApplication extends App with Controller with Bootstrap {

  protected val environment = Environment.Development

  def helloWorld = SimpleAction {
    request =>
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

  override protected val routingFilterOpt = Some(RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> helloWorld,
        Get % "/user/:userId" -> getUser,
        Get % ("""/group/(.*)""".r, Seq("name")) -> {
          request =>
            getGroup(request.routeParams("name"))(request)
        }
      )
  })

  await(start())

}
