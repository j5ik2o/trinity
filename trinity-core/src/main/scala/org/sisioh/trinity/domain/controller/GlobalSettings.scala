package org.sisioh.trinity.domain.controller

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.sisioh.trinity.domain.http.Request
import org.sisioh.trinity.application.TrinityApplication

trait GlobalSettings {

  def notFound(request: Request): Future[Response]

  def error(request: Request): Future[Response]

  def onStart(application: TrinityApplication) {}

  def onStop(application: TrinityApplication) {}

}
