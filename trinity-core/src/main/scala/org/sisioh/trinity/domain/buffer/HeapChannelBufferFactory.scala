package org.sisioh.trinity.domain.buffer

import java.nio.ByteOrder
import org.jboss.netty.buffer.{HeapChannelBufferFactory => NettyHeapChannelBufferFactory}

object HeapChannelBufferFactory {

  def apply(): ChannelBufferFactory =
    ChannelBufferFactory.from(NettyHeapChannelBufferFactory.getInstance())

  def apply(endianness: ByteOrder): ChannelBufferFactory =
    ChannelBufferFactory.from(NettyHeapChannelBufferFactory.getInstance(endianness))

}
