package org.sisioh.trinity.domain.io.buffer

import java.nio.ByteOrder
import org.jboss.netty.buffer.{DirectChannelBufferFactory => NettyDirectChannelBufferFactory}
import org.sisioh.trinity.domain.io.buffer.ChannelBufferFactory.toTrinity


object DirectChannelBufferFactory {

  def apply(): ChannelBufferFactory =
    NettyDirectChannelBufferFactory.getInstance()

  def apply(endianness: ByteOrder): ChannelBufferFactory =
    NettyDirectChannelBufferFactory.getInstance(endianness)

}
