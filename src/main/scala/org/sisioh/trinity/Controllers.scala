package org.sisioh.trinity

import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.util.Future
import scala.collection.mutable.ListBuffer

class Controllers {
  private val controllers = ListBuffer[Controller]()

  def dispatch(request: FinagleRequest): Option[Future[FinagleResponse]] = {
    val requestAdaptor = RequestAdaptor(request)
    controllers.find {
      _.findRoute(requestAdaptor, request.method).isDefined
    }.flatMap {
      _.dispatch(request)
    }
  }

  def add(controller: Controller) {
    controllers.append(controller)
  }

}
