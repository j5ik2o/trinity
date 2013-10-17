package org.sisioh.trinity.domain.mvc.http

import com.twitter.finagle.http.{Response => FinagleResponse}
import org.sisioh.trinity.domain.io.transport.codec.http
import org.sisioh.trinity.domain.io.transport.codec.http.{Response => IOResponse, ResponseStatus, Version, ResponseProxy}

trait Response extends Message with ResponseProxy {

  val finagle = FinagleResponse(netty)

}

object Response {

  def apply(underlying: http.Response): Response = new ResponseImpl(underlying)

  def apply(status: ResponseStatus.Value = ResponseStatus.Ok, version: Version.Value = Version.Http11): Response =
    new ResponseImpl(status, version)

}
