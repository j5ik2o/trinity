package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.mvc.controller.ScalatraLikeControllerSupport
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter

trait BootstrapWithScalatraLikeControllerSupport extends Bootstrap with ScalatraLikeControllerSupport {

  override protected val routingFilter =
    Some(RoutingFilter.createForControllers(this))

}
