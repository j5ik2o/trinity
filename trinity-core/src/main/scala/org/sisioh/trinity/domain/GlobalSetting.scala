package org.sisioh.trinity.domain

import com.twitter.util.Future
import com.twitter.finagle.http.Response

trait GlobalSetting {

  def notFound(request: RequestAdaptor): Future[Response]

  def error(request: RequestAdaptor): Future[Response]

}
