package org.sisioh.trinity.domain.io.buffer

import java.nio.{ByteBuffer, ByteOrder}
import org.jboss.netty.buffer.{ChannelBuffers => NettyChannelBuffers}
import org.sisioh.trinity.domain.io.http.Charset

/**
 * Represents the implementation class for [[ChannelBuffer]].
 */
private[buffer]
object ChannelBuffersImpl extends ChannelBuffers {

  def buffer(capacity: Int): ChannelBuffer = NettyChannelBuffers.buffer(capacity)

  def buffer(endianness: ByteOrder, capacity: Int): ChannelBuffer =
    NettyChannelBuffers.buffer(endianness, capacity)

  def directBuffer(capacity: Int): ChannelBuffer =
    NettyChannelBuffers.directBuffer(capacity)

  def directBuffer(endianness: ByteOrder, capacity: Int): ChannelBuffer =
    NettyChannelBuffers.directBuffer(endianness, capacity)

  def dynamicBuffer: ChannelBuffer =
    NettyChannelBuffers.dynamicBuffer()

  def dynamicBuffer(factory: ChannelBufferFactory): ChannelBuffer =
    NettyChannelBuffers.dynamicBuffer(factory)

  def dynamicBuffer(estimatedLength: Int): ChannelBuffer =
    NettyChannelBuffers.dynamicBuffer(estimatedLength)

  def dynamicBuffer(endianness: ByteOrder, estimatedLength: Int): ChannelBuffer =
    NettyChannelBuffers.dynamicBuffer(endianness, estimatedLength)

  def dynamicBuffer(estimatedLength: Int, factory: ChannelBufferFactory): ChannelBuffer =
    NettyChannelBuffers.dynamicBuffer(estimatedLength, factory)

  def dynamicBuffer(endianness: ByteOrder, estimatedLength: Int, factory: ChannelBufferFactory): ChannelBuffer =
    NettyChannelBuffers.dynamicBuffer(endianness, estimatedLength, factory)

  def wrappedBuffer(array: Array[Byte]): ChannelBuffer =
    NettyChannelBuffers.wrappedBuffer(array)

  def wrappedBuffer(endianness: ByteOrder, array: Array[Byte]): ChannelBuffer =
    NettyChannelBuffers.wrappedBuffer(endianness, array)

  def wrappedBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    NettyChannelBuffers.wrappedBuffer(array, offset, length)

  def wrappedBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    NettyChannelBuffers.wrappedBuffer(endianness, array, offset, length)

  def wrappedBuffer(array: ByteBuffer): ChannelBuffer =
    NettyChannelBuffers.wrappedBuffer(array)

  def wrappedBuffer(array: ChannelBuffer): ChannelBuffer =
    NettyChannelBuffers.wrappedBuffer(array)

  def wrappedBuffersFromByteArray(arrays: Array[Byte]*): ChannelBuffer =
    ChannelBuffer.toTrinity(NettyChannelBuffers.wrappedBuffer(arrays: _*))

  def wrappedBuffersFromByteArray(endianness: ByteOrder, arrays: Array[Byte]*): ChannelBuffer =
    ChannelBuffer.toTrinity(NettyChannelBuffers.wrappedBuffer(endianness, arrays: _*))

  def wrappedBuffers(arrays: ChannelBuffer*): ChannelBuffer =
    ChannelBuffer.toTrinity(NettyChannelBuffers.wrappedBuffer(arrays.map(ChannelBuffer.toNetty): _*))

  def wrappedBuffers(gathering: Boolean, arrays: ChannelBuffer*): ChannelBuffer =
    NettyChannelBuffers.wrappedBuffer(gathering, arrays.map(ChannelBuffer.toNetty): _*)

  def wrappedBuffersFromByteBuffer(arrays: ByteBuffer*): ChannelBuffer =
    ChannelBuffer.toTrinity(NettyChannelBuffers.wrappedBuffer(arrays: _*))

  def wrappedBuffersFromByteBuffer(gathering: Boolean, arrays: ByteBuffer*): ChannelBuffer =
    NettyChannelBuffers.wrappedBuffer(gathering, arrays: _*)

  def copiedBuffer(array: Array[Byte]): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(array)

  def copiedBuffer(endianness: ByteOrder, array: Array[Byte]): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(endianness, array)

  def copiedBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(array, offset, length)

  def copiedBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(endianness, array, offset, length)

  def copiedBuffer(buffer: ByteBuffer): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(buffer)

  def copiedBuffer(buffer: ChannelBuffer): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(buffer)

  def copiedBuffersFromByteArray(arrays: Array[Byte]*): ChannelBuffer =
    ChannelBuffer.toTrinity(NettyChannelBuffers.copiedBuffer(arrays: _*))

  def copiedBuffersFromByteArray(endianness: ByteOrder, arrays: Array[Byte]*): ChannelBuffer =
    ChannelBuffer.toTrinity(NettyChannelBuffers.copiedBuffer(endianness, arrays: _*))

  def copiedBuffers(buffers: ChannelBuffer*): ChannelBuffer =
    ChannelBuffer.toTrinity(NettyChannelBuffers.copiedBuffer(buffers.map(ChannelBuffer.toNetty): _*))

  def copiedBuffersFromByteBuffer(buffers: ByteBuffer*): ChannelBuffer =
    ChannelBuffer.toTrinity(NettyChannelBuffers.copiedBuffer(buffers: _*))

  def copiedBuffer(string: CharSequence, charset: Charset): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(string, charset.toObject)

  def copiedBuffer(string: CharSequence, offset: Int, length: Int, charset: Charset): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(string, offset, length, charset.toObject)

  def copiedBuffer(endianness: ByteOrder, string: CharSequence, charset: Charset): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(endianness, string, charset.toObject)

  def copiedBuffer(endianness: ByteOrder, string: CharSequence, offset: Int, length: Int, charset: Charset): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(endianness, string, offset, length, charset.toObject)

  def copiedBuffer(array: Array[Char], charset: Charset): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(array, charset.toObject)

  def copiedBuffer(array: Array[Char], offset: Int, length: Int, charset: Charset): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(array, offset, length, charset.toObject)

  def copiedBuffer(endianness: ByteOrder, array: Array[Char], charset: Charset): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(endianness, array, charset.toObject)

  def copiedBuffer(endianness: ByteOrder, array: Array[Char], offset: Int, length: Int, charset: Charset): ChannelBuffer =
    NettyChannelBuffers.copiedBuffer(endianness, array, offset, length, charset.toObject)

}
