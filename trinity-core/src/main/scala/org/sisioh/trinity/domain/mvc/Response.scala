package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.transport.codec.http.ResponseProxy

trait Response extends ResponseProxy {

  val body: ChannelBuffer = underlying.content

  def bodyAsString: String = body.toString

}
