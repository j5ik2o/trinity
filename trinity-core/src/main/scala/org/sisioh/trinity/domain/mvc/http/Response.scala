package org.sisioh.trinity.domain.mvc.http

import com.twitter.finagle.http.{ Response => FinagleResponse }
import org.sisioh.trinity.domain.io.http
import org.sisioh.trinity.domain.io.http.{ Response => IOResponse, ResponseStatus, ProtocolVersion, ResponseProxy }

trait Response extends Message with ResponseProxy {

  def encodeString(): String = toUnderlyingAsFinagle.encodeString()

}

object Response {

  def apply(underlying: http.Response): Response = new ResponseImpl(underlying)

  def apply(responseStatus: ResponseStatus.Value = ResponseStatus.Ok,
            protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11): Response =
    new ResponseImpl(responseStatus, protocolVersion)

}
