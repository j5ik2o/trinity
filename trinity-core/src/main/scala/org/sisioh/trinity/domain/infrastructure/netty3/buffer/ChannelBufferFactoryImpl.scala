package org.sisioh.trinity.domain.infrastructure.netty3.buffer

import org.sisioh.trinity.domain.buffer.{ChannelBuffer, ChannelBufferFactory}
import org.jboss.netty.buffer.{ChannelBufferFactory => NettyChannelBufferFactory}
import java.nio.{ByteBuffer, ByteOrder}

case class ChannelBufferFactoryImpl(underlying: NettyChannelBufferFactory)
  extends ChannelBufferFactory {

  def getBuffer(capacity: Int) = ChannelBuffer.from(underlying.getBuffer(capacity))

  def getBuffer(endianness: ByteOrder, capacity: Int): ChannelBuffer =
    ChannelBuffer.from(underlying.getBuffer(endianness, capacity))

  def getBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    ChannelBuffer.from(underlying.getBuffer(array, offset, length))

  def getBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    ChannelBuffer.from(underlying.getBuffer(endianness, array, offset, length))

  def getBuffer(nioBuffer: ByteBuffer): ChannelBuffer = ChannelBuffer.from(underlying.getBuffer(nioBuffer))

  def getDefaultOrder: ByteOrder = underlying.getDefaultOrder

}
