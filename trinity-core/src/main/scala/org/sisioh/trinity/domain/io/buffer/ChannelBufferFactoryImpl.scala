package org.sisioh.trinity.domain.io.buffer

import java.nio.{ByteBuffer, ByteOrder}
import org.jboss.netty.buffer.{ChannelBufferFactory => NettyChannelBufferFactory}

private[trinity]
case class ChannelBufferFactoryImpl(underlying: NettyChannelBufferFactory)
  extends ChannelBufferFactory {

  def getBuffer(capacity: Int) = underlying.getBuffer(capacity)

  def getBuffer(endianness: ByteOrder, capacity: Int): ChannelBuffer =
    underlying.getBuffer(endianness, capacity)

  def getBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    underlying.getBuffer(array, offset, length)

  def getBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    underlying.getBuffer(endianness, array, offset, length)

  def getBuffer(nioBuffer: ByteBuffer): ChannelBuffer = underlying.getBuffer(nioBuffer)

  def getDefaultOrder: ByteOrder = underlying.getDefaultOrder

}
