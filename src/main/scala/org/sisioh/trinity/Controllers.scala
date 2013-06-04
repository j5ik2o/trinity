package org.sisioh.trinity

import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.util.Future
import scala.collection.mutable.ListBuffer

class Controllers {
  private val controllers = ListBuffer[Controller]()

  def render = new ResponseBuilder

  def dispatch(request: FinagleRequest): Option[Future[FinagleResponse]] = {
    controllers.find {
      _.dispatch(request).isDefined
    }.flatMap {
      _.dispatch(request)
    }
  }

  def add(controller: Controller) {
    controllers.append(controller)
  }

}
