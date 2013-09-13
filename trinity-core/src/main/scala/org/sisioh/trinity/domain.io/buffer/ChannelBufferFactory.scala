package org.sisioh.trinity.domain.io.buffer

import java.nio.{ByteBuffer, ByteOrder}
import org.jboss.netty.buffer.{ChannelBufferFactory => NettyChannelBufferFactory}
import org.sisioh.trinity.domain.io.infrastructure.netty3.buffer.ChannelBufferFactoryImpl

/**
 * [[org.sisioh.trinity.domain.buffer.ChannelBuffer]]のためのファクトリ。
 */
trait ChannelBufferFactory {

  def getBuffer(capacity: Int): ChannelBuffer

  def getBuffer(endianness: ByteOrder, capacity: Int): ChannelBuffer

  def getBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer

  def getBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer

  def getBuffer(nioBuffer: ByteBuffer): ChannelBuffer

  def getDefaultOrder: ByteOrder

}

object ChannelBufferFactory {

  private[trinity] def from(underlying: NettyChannelBufferFactory): ChannelBufferFactory =
    ChannelBufferFactoryImpl(underlying)

}