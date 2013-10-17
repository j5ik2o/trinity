package org.sisioh.trinity.domain.mvc.http

import com.twitter.finagle.http.{ Response => FinagleResponse }
import org.sisioh.trinity.domain.io.http
import org.sisioh.trinity.domain.io.http.{ Response => IOResponse, ResponseStatus, Version, ResponseProxy }

trait Response extends Message with ResponseProxy {

  lazy val finagle = FinagleResponse(netty)

  def encodeString(): String = finagle.encodeString()

}

object Response {

  def apply(underlying: http.Response): Response = new ResponseImpl(underlying)

  def apply(status: ResponseStatus.Value = ResponseStatus.Ok, version: Version.Value = Version.Http11): Response =
    new ResponseImpl(status, version)

}
