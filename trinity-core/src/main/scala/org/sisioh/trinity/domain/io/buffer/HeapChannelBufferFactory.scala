package org.sisioh.trinity.domain.io.buffer

import java.nio.ByteOrder
import org.jboss.netty.buffer.{HeapChannelBufferFactory => NettyHeapChannelBufferFactory}
import org.sisioh.trinity.domain.io.buffer.ChannelBufferFactory.toTrinity

/**
 * Represents a factory class for `org.jboss.netty.buffer.NettyHeapChannelBufferFactory`.
 */
object HeapChannelBufferFactory {

  /**
   * Creates a Heap [[ChannelBufferFactory]]'s instance.
   *
   * @return Heap [[ChannelBufferFactory]]
   */
  def apply(): ChannelBufferFactory =
    NettyHeapChannelBufferFactory.getInstance()

  /**
   * Creates a Heap [[ChannelBufferFactory]]'s instance.
   *
   * @param endianness `ByteOrder`
   * @return Heap [[ChannelBufferFactory]]
   */
  def apply(endianness: ByteOrder): ChannelBufferFactory =
    NettyHeapChannelBufferFactory.getInstance(endianness)

}
