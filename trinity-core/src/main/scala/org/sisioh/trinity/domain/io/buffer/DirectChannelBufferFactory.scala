package org.sisioh.trinity.domain.io.buffer

import java.nio.ByteOrder
import org.jboss.netty.buffer.{DirectChannelBufferFactory => NettyDirectChannelBufferFactory}
import org.sisioh.trinity.domain.io.buffer.ChannelBufferFactory.toTrinity

/**
 * Represents a factory for `org.jboss.netty.buffer.DirectChannelBuffer`.
 */
object DirectChannelBufferFactory {

  /**
   * Creates a Direct [[ChannelBufferFactory]]'s instance.
   *
   * @return Direct [[ChannelBufferFactory]]
   */
  def apply(): ChannelBufferFactory =
    NettyDirectChannelBufferFactory.getInstance()

  /**
   * Creates a Direct [[ChannelBufferFactory]]'s instance.
   *
   * @param endianness [[ByteOrder]]
   * @return Direct [[ChannelBufferFactory]]
   */
  def apply(endianness: ByteOrder): ChannelBufferFactory =
    NettyDirectChannelBufferFactory.getInstance(endianness)

}
