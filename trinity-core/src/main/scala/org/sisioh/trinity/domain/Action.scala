package org.sisioh.trinity.domain

import com.twitter.util.Future
import com.twitter.finagle.http.Response


trait Action extends (Request => Future[Response])

object Action {

  def apply(action: Request => Future[Response]) = new Action {
    def apply(request: Request): Future[Response] = action(request)
  }

}
