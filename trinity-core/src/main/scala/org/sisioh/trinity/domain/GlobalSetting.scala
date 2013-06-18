package org.sisioh.trinity.domain

import com.twitter.util.Future
import com.twitter.finagle.http.Response

trait GlobalSetting {

  def notFound(request: Request): Future[Response]

  def error(request: Request): Future[Response]

}
