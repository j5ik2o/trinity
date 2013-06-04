package org.sisioh.trinity

import com.twitter.util.Future

trait GlobalSetting {

  def notFound(request: Request): Future[Response]

  def error(request: Request): Future[Response]

}
