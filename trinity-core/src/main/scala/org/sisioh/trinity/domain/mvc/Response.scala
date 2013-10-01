package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.buffer.ChannelBuffers
import org.sisioh.trinity.domain.io.transport.codec.http
import org.sisioh.trinity.domain.io.transport.codec.http.{Response => IOResponse, CharsetUtil, ResponseStatus, Version, ResponseProxy}

trait Response extends ResponseProxy {

  def contentAsString: String = content.toString

  def withContentAsString(body: String): this.type =
    withContent(ChannelBuffers.copiedBuffer(body, CharsetUtil.UTF_8))

}

object Response {

  def apply(underlying: http.Response): Response = new ResponseImpl(underlying)

  def apply(version: Version.Value = Version.Http11, status: ResponseStatus.Value = ResponseStatus.Ok): Response = new ResponseImpl(version, status)

}
