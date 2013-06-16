package org.sisioh.trinity.domain

import com.twitter.util.Future
import org.sisioh.trinity.{ResponseBuilder, RequestAdaptor}

trait GlobalSetting {

  def notFound(request: RequestAdaptor): Future[ResponseBuilder]

  def error(request: RequestAdaptor): Future[ResponseBuilder]

}
