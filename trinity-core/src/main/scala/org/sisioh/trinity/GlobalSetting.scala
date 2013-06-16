package org.sisioh.trinity

import com.twitter.util.Future

trait GlobalSetting {

  def notFound(request: RequestAdaptor): Future[ResponseBuilder]

  def error(request: RequestAdaptor): Future[ResponseBuilder]

}
