package org.sisioh.trinity.domain

import com.twitter.util.Future
import com.twitter.finagle.http.Response


trait Action extends (RequestAdaptor => Future[Response])

object Action {
  def apply(action: RequestAdaptor => Future[Response]) = new Action {
    def apply(request: RequestAdaptor): Future[Response] = action(request)
  }
}
