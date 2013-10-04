package org.sisioh.trinity.domain.mvc.resource

import org.sisioh.trinity.domain.mvc.SimpleFilter
import org.sisioh.trinity.domain.mvc.http.{Request, Response}
import org.sisioh.trinity.domain.mvc.action.Action
import scala.concurrent.Future

class FileResourceReadFilter extends SimpleFilter[Request, Response] {
  def apply(requestIn: Request, action: Action[Request, Response]): Future[Response] = ???
}
