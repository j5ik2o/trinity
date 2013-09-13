package org.sisioh.trinity.domain.io.infrastructure.netty3.buffer

import org.specs2.mutable.Specification
import org.sisioh.trinity.domain.io.buffer.ChannelBuffers

class ChannelBufferImplSpec extends Specification {

  val target = ChannelBuffers.buffer(100)

  "channelBuffer" should {
    "" in {
      val copyInstance = target.withWriteChar('a')
      println("value = " + new String(copyInstance.array) + ":")
      true must_== true
    }
  }

}
