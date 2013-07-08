package org.sisioh.trinity.domain.controller

import org.sisioh.trinity.domain.routing.Action
import org.sisioh.trinity.domain.http.TrinityRequest
import com.twitter.util.Future
import com.twitter.finagle.http.Response

case object ErrorUnHandleAction extends Action {

  def apply(request: TrinityRequest): Future[Response] = {
    Future.exception(TrinityException(Some(s"Error Request is $request"), request.error))
  }

}
