package org.sisioh.trinity.domain.controller

import org.sisioh.trinity.domain.routing.Action
import org.sisioh.trinity.domain.http.TrinityRequest
import com.twitter.util.Future
import com.twitter.finagle.http.Response

case object NotFoundUnHandleAction extends Action {

  def apply(request: TrinityRequest): Future[Response] = {
    Future.exception(ActionNotFoundException(Some(request.toString())))
  }

}
