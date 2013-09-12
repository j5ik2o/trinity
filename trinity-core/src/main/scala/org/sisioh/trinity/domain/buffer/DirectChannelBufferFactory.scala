package org.sisioh.trinity.domain.buffer

import org.jboss.netty.buffer.{DirectChannelBufferFactory => NettyDirectChannelBufferFactory}
import java.nio.ByteOrder

object DirectChannelBufferFactory {

  def apply(): ChannelBufferFactory =
    ChannelBufferFactory.from(NettyDirectChannelBufferFactory.getInstance())

  def apply(endianness: ByteOrder): ChannelBufferFactory =
    ChannelBufferFactory.from(NettyDirectChannelBufferFactory.getInstance(endianness))

}
