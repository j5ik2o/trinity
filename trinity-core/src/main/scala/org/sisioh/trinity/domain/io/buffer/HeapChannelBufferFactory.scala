package org.sisioh.trinity.domain.io.buffer

import java.nio.ByteOrder
import org.jboss.netty.buffer.{HeapChannelBufferFactory => NettyHeapChannelBufferFactory}
import org.sisioh.trinity.domain.io.buffer.ChannelBufferFactory.toTrinity

/**
 * HeapChannelBufferのためのファクトリ。
 */
object HeapChannelBufferFactory {

  def apply(): ChannelBufferFactory =
    NettyHeapChannelBufferFactory.getInstance()

  def apply(endianness: ByteOrder): ChannelBufferFactory =
    NettyHeapChannelBufferFactory.getInstance(endianness)

}
