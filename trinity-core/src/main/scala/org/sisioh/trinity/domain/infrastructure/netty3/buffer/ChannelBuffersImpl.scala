package org.sisioh.trinity.domain.infrastructure.netty3.buffer

import java.nio.charset.Charset
import java.nio.{ByteBuffer, ByteOrder}
import org.jboss.netty.buffer.{ChannelBuffers => NettyChannelBuffers, ChannelBuffer => NettyChannelBuffer, ChannelBufferFactory => NettyChannelBufferFactory}
import org.sisioh.trinity.domain.buffer.{ChannelBufferFactory, ChannelBuffer, ChannelBuffers}

object ChannelBuffersImpl extends ChannelBuffers {

  implicit def toUnderlying(target: ChannelBuffer): NettyChannelBuffer = target match {
    case ncb: ChannelBufferImpl =>
      ncb.underlying
    case _ =>
      throw new IllegalArgumentException()
  }

  implicit def toUnderlying(target: ChannelBufferFactory): NettyChannelBufferFactory = target match {
    case ncb: ChannelBufferFactoryImpl =>
      ncb.underlying
    case _ =>
      throw new IllegalArgumentException()
  }

  def buffer(capacity: Int): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.buffer(capacity))

  def buffer(endianness: ByteOrder, capacity: Int): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.buffer(endianness, capacity))

  def directBuffer(capacity: Int): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.directBuffer(capacity))

  def directBuffer(endianness: ByteOrder, capacity: Int): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.directBuffer(endianness, capacity))

  def dynamicBuffer: ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.dynamicBuffer())

  def dynamicBuffer(factory: ChannelBufferFactory): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.dynamicBuffer(factory))

  def dynamicBuffer(estimatedLength: Int): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.dynamicBuffer(estimatedLength))

  def dynamicBuffer(endianness: ByteOrder, estimatedLength: Int): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.dynamicBuffer(endianness, estimatedLength))

  def dynamicBuffer(estimatedLength: Int, factory: ChannelBufferFactory): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.dynamicBuffer(estimatedLength, factory))

  def dynamicBuffer(endianness: ByteOrder, estimatedLength: Int, factory: ChannelBufferFactory): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.dynamicBuffer(endianness, estimatedLength, factory))

  def wrappedBuffer(array: Array[Byte]): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(array))

  def wrappedBuffer(endianness: ByteOrder, array: Array[Byte]): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(endianness, array))

  def wrappedBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(array, offset, length))

  def wrappedBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(endianness, array, offset, length))

  def wrappedBuffer(array: ByteBuffer): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(array))

  def wrappedBuffer(array: ChannelBuffer): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(array))

  def wrappedBuffersFromByteArray(arrays: Array[Byte]*): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(arrays: _*))

  def wrappedBuffersFromByteArray(endianness: ByteOrder, arrays: Array[Byte]*): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(endianness, arrays: _*))

  def wrappedBuffers(arrays: ChannelBuffer*): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(arrays.map(toUnderlying): _*))

  def wrappedBuffers(gathering: Boolean, arrays: ChannelBuffer*): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(gathering, arrays.map(toUnderlying): _*))

  def wrappedBuffersFromByteBuffer(arrays: ByteBuffer*): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(arrays: _*))

  def wrappedBuffersFromByteBuffer(gathering: Boolean, arrays: ByteBuffer*): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.wrappedBuffer(gathering, arrays: _*))

  def copiedBuffer(array: Array[Byte]): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(array))

  def copiedBuffer(endianness: ByteOrder, array: Array[Byte]): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(endianness, array))

  def copiedBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(array, offset, length))

  def copiedBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(endianness, array, offset, length))

  def copiedBuffer(buffer: ByteBuffer): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(buffer))

  def copiedBuffer(buffer: ChannelBuffer): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(buffer))

  def copiedBuffersFromByteArray(arrays: Array[Byte]*): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(arrays: _*))

  def copiedBuffersFromByteArray(endianness: ByteOrder, arrays: Array[Byte]*): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(endianness, arrays: _*))

  def copiedBuffers(buffers: ChannelBuffer*): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(buffers.map(toUnderlying): _*))

  def copiedBuffersFromByteBuffer(buffers: ByteBuffer*): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(buffers: _*))

  def copiedBuffer(string: CharSequence, charset: Charset): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(string, charset))

  def copiedBuffer(string: CharSequence, offset: Int, length: Int, charset: Charset): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(string, offset, length, charset))

  def copiedBuffer(endianness: ByteOrder, string: CharSequence, charset: Charset): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(endianness, string, charset))

  def copiedBuffer(endianness: ByteOrder, string: CharSequence, offset: Int, length: Int, charset: Charset): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(endianness, string, offset, length, charset))

  def copiedBuffer(array: Array[Char], charset: Charset): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(array, charset))

  def copiedBuffer(array: Array[Char], offset: Int, length: Int, charset: Charset): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(array, offset, length, charset))

  def copiedBuffer(endianness: ByteOrder, array: Array[Char], charset: Charset): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(endianness, array, charset))

  def copiedBuffer(endianness: ByteOrder, array: Array[Char], offset: Int, length: Int, charset: Charset): ChannelBuffer =
    ChannelBuffer.from(NettyChannelBuffers.copiedBuffer(endianness, array, offset, length, charset))

}
