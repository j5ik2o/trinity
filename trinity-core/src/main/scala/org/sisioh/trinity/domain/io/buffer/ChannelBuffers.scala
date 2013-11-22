package org.sisioh.trinity.domain.io.buffer

import java.nio._
import java.nio.charset.Charset

import scala.language.implicitConversions

import org.jboss.netty.buffer.{ ChannelBufferFactory => NettyChannelBufferFactory }
import org.sisioh.trinity.domain.io.infrastructure.buffer.ChannelBufferFactoryImpl
import org.sisioh.trinity.domain.io.infrastructure.buffer.ChannelBuffersImpl

trait ChannelBuffers {

  // ---

  def buffer(capacity: Int): ChannelBuffer

  def buffer(endianness: ByteOrder, capacity: Int): ChannelBuffer

  def directBuffer(capacity: Int): ChannelBuffer

  def directBuffer(endianness: ByteOrder, capacity: Int): ChannelBuffer

  // ---

  def dynamicBuffer: ChannelBuffer

  def dynamicBuffer(factory: ChannelBufferFactory): ChannelBuffer

  def dynamicBuffer(estimatedLength: Int): ChannelBuffer

  def dynamicBuffer(endianness: ByteOrder, estimatedLength: Int): ChannelBuffer

  def dynamicBuffer(estimatedLength: Int, factory: ChannelBufferFactory): ChannelBuffer

  def dynamicBuffer(endianness: ByteOrder, estimatedLength: Int, factory: ChannelBufferFactory): ChannelBuffer

  // ---

  def wrappedBuffer(array: Array[Byte]): ChannelBuffer

  def wrappedBuffer(endianness: ByteOrder, array: Array[Byte]): ChannelBuffer

  def wrappedBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer

  def wrappedBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer

  def wrappedBuffer(buffer: ByteBuffer): ChannelBuffer

  def wrappedBuffer(buffer: ChannelBuffer): ChannelBuffer

  def wrappedBuffersFromByteArray(arrays: Array[Byte]*): ChannelBuffer

  def wrappedBuffersFromByteArray(endianness: ByteOrder, arrays: Array[Byte]*): ChannelBuffer

  def wrappedBuffers(buffers: ChannelBuffer*): ChannelBuffer

  def wrappedBuffers(gathering: Boolean, buffers: ChannelBuffer*): ChannelBuffer

  def wrappedBuffersFromByteBuffer(buffers: ByteBuffer*): ChannelBuffer

  def wrappedBuffersFromByteBuffer(gathering: Boolean, buffers: ByteBuffer*): ChannelBuffer

  // ---

  def copiedBuffer(array: Array[Byte]): ChannelBuffer

  def copiedBuffer(endianness: ByteOrder, array: Array[Byte]): ChannelBuffer

  def copiedBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer

  def copiedBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer

  def copiedBuffer(buffer: ByteBuffer): ChannelBuffer

  def copiedBuffer(buffer: ChannelBuffer): ChannelBuffer

  def copiedBuffersFromByteArray(arrays: Array[Byte]*): ChannelBuffer

  def copiedBuffersFromByteArray(endianness: ByteOrder, arrays: Array[Byte]*): ChannelBuffer

  def copiedBuffers(buffers: ChannelBuffer*): ChannelBuffer

  def copiedBuffersFromByteBuffer(buffers: ByteBuffer*): ChannelBuffer

  def copiedBuffer(string: CharSequence, charset: Charset): ChannelBuffer

  def copiedBuffer(string: CharSequence, offset: Int, length: Int, charset: Charset): ChannelBuffer

  def copiedBuffer(endianness: ByteOrder, string: CharSequence, charset: Charset): ChannelBuffer

  def copiedBuffer(endianness: ByteOrder, string: CharSequence, offset: Int, length: Int, charset: Charset): ChannelBuffer

  def copiedBuffer(array: Array[Char], charset: Charset): ChannelBuffer

  def copiedBuffer(array: Array[Char], offset: Int, length: Int, charset: Charset): ChannelBuffer

  def copiedBuffer(endianness: ByteOrder, array: Array[Char], charset: Charset): ChannelBuffer

  def copiedBuffer(endianness: ByteOrder, array: Array[Char], offset: Int, length: Int, charset: Charset): ChannelBuffer

}

object ChannelBuffers extends ChannelBuffers {

  implicit def toNetty(target: ChannelBufferFactory): NettyChannelBufferFactory = target match {
    case ncb: ChannelBufferFactoryImpl =>
      ncb.underlying
    case _ =>
      throw new IllegalArgumentException()
  }

  private val impl: ChannelBuffers = ChannelBuffersImpl

  def buffer(capacity: Int): ChannelBuffer = impl.buffer(capacity)

  def buffer(endianness: ByteOrder, capacity: Int): ChannelBuffer =
    impl.buffer(endianness, capacity)

  def directBuffer(capacity: Int): ChannelBuffer =
    impl.directBuffer(capacity)

  def directBuffer(endianness: ByteOrder, capacity: Int): ChannelBuffer =
    impl.directBuffer(endianness, capacity)

  def dynamicBuffer: ChannelBuffer =
    impl.dynamicBuffer

  def dynamicBuffer(factory: ChannelBufferFactory): ChannelBuffer =
    impl.dynamicBuffer(factory)

  def dynamicBuffer(estimatedLength: Int): ChannelBuffer =
    impl.dynamicBuffer(estimatedLength)

  def dynamicBuffer(endianness: ByteOrder, estimatedLength: Int): ChannelBuffer =
    impl.dynamicBuffer(endianness, estimatedLength)

  def dynamicBuffer(estimatedLength: Int, factory: ChannelBufferFactory): ChannelBuffer =
    impl.dynamicBuffer(estimatedLength, factory)

  def dynamicBuffer(endianness: ByteOrder, estimatedLength: Int, factory: ChannelBufferFactory): ChannelBuffer =
    impl.dynamicBuffer(endianness, estimatedLength, factory)

  def wrappedBuffer(array: Array[Byte]): ChannelBuffer =
    impl.wrappedBuffer(array)

  def wrappedBuffer(endianness: ByteOrder, array: Array[Byte]): ChannelBuffer =
    impl.wrappedBuffer(endianness, array)

  def wrappedBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    impl.wrappedBuffer(array, offset, length)

  def wrappedBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    impl.wrappedBuffer(endianness, array, offset, length)

  def wrappedBuffer(buffer: ByteBuffer): ChannelBuffer =
    impl.wrappedBuffer(buffer)

  def wrappedBuffer(buffer: ChannelBuffer): ChannelBuffer =
    impl.wrappedBuffer(buffer)

  def wrappedBuffersFromByteArray(arrays: Array[Byte]*): ChannelBuffer =
    impl.wrappedBuffersFromByteArray(arrays: _*)

  def wrappedBuffersFromByteArray(endianness: ByteOrder, arrays: Array[Byte]*): ChannelBuffer =
    impl.wrappedBuffersFromByteArray(endianness, arrays: _*)

  def wrappedBuffers(buffers: ChannelBuffer*): ChannelBuffer =
    impl.wrappedBuffers(buffers: _*)

  def wrappedBuffers(gathering: Boolean, buffers: ChannelBuffer*): ChannelBuffer =
    impl.wrappedBuffers(gathering, buffers: _*)

  def wrappedBuffersFromByteBuffer(buffers: ByteBuffer*): ChannelBuffer =
    impl.wrappedBuffersFromByteBuffer(buffers: _*)

  def wrappedBuffersFromByteBuffer(gathering: Boolean, buffers: ByteBuffer*): ChannelBuffer =
    impl.wrappedBuffersFromByteBuffer(gathering, buffers: _*)

  def copiedBuffer(array: Array[Byte]): ChannelBuffer =
    impl.copiedBuffer(array)

  def copiedBuffer(endianness: ByteOrder, array: Array[Byte]): ChannelBuffer =
    impl.copiedBuffer(endianness, array)

  def copiedBuffer(array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    impl.copiedBuffer(array, offset, length)

  def copiedBuffer(endianness: ByteOrder, array: Array[Byte], offset: Int, length: Int): ChannelBuffer =
    impl.copiedBuffer(endianness, array, offset, length)

  def copiedBuffer(buffer: ByteBuffer): ChannelBuffer =
    impl.copiedBuffer(buffer)

  def copiedBuffer(buffer: ChannelBuffer): ChannelBuffer =
    impl.copiedBuffer(buffer)

  def copiedBuffersFromByteArray(arrays: Array[Byte]*): ChannelBuffer =
    impl.copiedBuffersFromByteArray(arrays: _*)

  def copiedBuffersFromByteArray(endianness: ByteOrder, arrays: Array[Byte]*): ChannelBuffer =
    impl.copiedBuffersFromByteArray(endianness, arrays: _*)

  def copiedBuffers(buffers: ChannelBuffer*): ChannelBuffer =
    impl.copiedBuffers(buffers: _*)

  def copiedBuffersFromByteBuffer(arrays: ByteBuffer*): ChannelBuffer =
    impl.copiedBuffersFromByteBuffer(arrays: _*)

  def copiedBuffer(string: CharSequence, charset: Charset): ChannelBuffer =
    impl.copiedBuffer(string, charset)

  def copiedBuffer(string: CharSequence, offset: Int, length: Int, charset: Charset): ChannelBuffer =
    impl.copiedBuffer(string, offset, length, charset)

  def copiedBuffer(endianness: ByteOrder, string: CharSequence, charset: Charset): ChannelBuffer =
    impl.copiedBuffer(endianness, string, charset)

  def copiedBuffer(endianness: ByteOrder, string: CharSequence, offset: Int, length: Int, charset: Charset): ChannelBuffer =
    impl.copiedBuffer(endianness, string, offset, length, charset)

  def copiedBuffer(array: Array[Char], charset: Charset): ChannelBuffer =
    impl.copiedBuffer(array, charset)

  def copiedBuffer(array: Array[Char], offset: Int, length: Int, charset: Charset): ChannelBuffer =
    impl.copiedBuffer(array, offset, length, charset)

  def copiedBuffer(endianness: ByteOrder, array: Array[Char], charset: Charset): ChannelBuffer =
    impl.copiedBuffer(endianness, array, charset)

  def copiedBuffer(endianness: ByteOrder, array: Array[Char], offset: Int, length: Int, charset: Charset): ChannelBuffer =
    impl.copiedBuffer(endianness, array, offset, length, charset)

}
