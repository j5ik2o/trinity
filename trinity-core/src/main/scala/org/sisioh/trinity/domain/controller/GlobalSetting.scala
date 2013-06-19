package org.sisioh.trinity.domain.controller

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.sisioh.trinity.domain.http.Request

trait GlobalSetting {

  def notFound(request: Request): Future[Response]

  def error(request: Request): Future[Response]

}
