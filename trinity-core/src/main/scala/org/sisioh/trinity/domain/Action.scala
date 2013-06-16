package org.sisioh.trinity.domain

import com.twitter.util.Future
import org.sisioh.trinity.{ResponseBuilder, RequestAdaptor}


trait Action extends (RequestAdaptor => Future[ResponseBuilder])

object Action {
  def apply(action: RequestAdaptor => Future[ResponseBuilder]) = new Action {
    def apply(request: RequestAdaptor): Future[ResponseBuilder] = action(request)
  }
}
