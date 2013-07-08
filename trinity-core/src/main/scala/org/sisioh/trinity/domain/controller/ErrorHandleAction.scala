package org.sisioh.trinity.domain.controller

import org.sisioh.trinity.domain.routing.Action
import org.sisioh.trinity.domain.http.{TrinityResponseBuilder, TrinityRequest}
import com.twitter.util.Future
import com.twitter.finagle.http.{Status, Response}

case object ErrorHandleAction extends Action {

  def apply(request: TrinityRequest): Future[Response] = {
    TrinityResponseBuilder().
      withStatus(Status.InternalServerError).
      withPlain("Internal Server Error").
      toFinagleResponseFuture
  }

}
