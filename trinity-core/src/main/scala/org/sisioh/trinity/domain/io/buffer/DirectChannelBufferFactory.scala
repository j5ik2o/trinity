package org.sisioh.trinity.domain.io.buffer

import java.nio.ByteOrder
import org.jboss.netty.buffer.{DirectChannelBufferFactory => NettyDirectChannelBufferFactory}

object DirectChannelBufferFactory {

  def apply(): ChannelBufferFactory =
    NettyDirectChannelBufferFactory.getInstance()

  def apply(endianness: ByteOrder): ChannelBufferFactory =
    NettyDirectChannelBufferFactory.getInstance(endianness)

}
