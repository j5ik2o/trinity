package org.sisioh.trinity.domain.mvc.application

import org.sisioh.trinity.domain.mvc.controller.ScalatraLikeControllerSupport
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter

trait ScalatraLikeApplicationSupport extends Bootstrap with ScalatraLikeControllerSupport {
  this: Application =>

  override protected lazy val routingFilter =
    Some(RoutingFilter.createForControllers(this))

}
