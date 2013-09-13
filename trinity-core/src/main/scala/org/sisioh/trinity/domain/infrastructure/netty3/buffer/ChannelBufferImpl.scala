package org.sisioh.trinity.domain.infrastructure.netty3.buffer

import java.io.InputStream
import java.nio.channels.ScatteringByteChannel
import java.nio.charset.Charset
import java.nio.{ByteOrder, ByteBuffer}
import org.jboss.netty.buffer.{ChannelBuffer => NettyChannelBuffer, ChannelBuffers}
import org.sisioh.trinity.domain.buffer.{ChannelBufferFactory, ChannelBuffer}
import scala.util.Try

private[trinity]
case class ChannelBufferImpl(underlying: NettyChannelBuffer, mutated: Boolean = false) extends ChannelBuffer {

  private def mutateAsThis(f: (NettyChannelBuffer) => Unit): ChannelBuffer = {
    val result = if (mutated) this else internalClone
    f(result.underlying)
    result
  }

  private def mutateAsResult[A](f: (NettyChannelBuffer) => A): A = {
    val result = if (mutated) this else internalClone
    f(result.underlying)
  }

  import ChannelBuffersImpl._

  def factory: ChannelBufferFactory = ChannelBufferFactory.from(underlying.factory())

  def capacity: Int = underlying.capacity()

  def byteOrder: ByteOrder = underlying.order()

  def isDirect: Boolean = underlying.isDirect

  def readerIndex: Int = underlying.readerIndex()

  def writerIndex: Int = underlying.writerIndex()

  def withReaderIndex(readerIndex: Int): ChannelBuffer = mutateAsThis {
    _.readerIndex(readerIndex)
  }

  def withWriterIndex(writerIndex: Int): ChannelBuffer = mutateAsThis {
    _.writerIndex(writerIndex)
  }

  def withIndex(readerIndex: Int, writerIndex: Int): ChannelBuffer = mutateAsThis {
    _.setIndex(readerIndex, writerIndex)
  }

  def readableBytes: Int = underlying.readableBytes()

  def writableBytes: Int = underlying.writableBytes()

  def isReadable: Boolean = underlying.readable()

  def isWritable: Boolean = underlying.writable()

  def withClear: ChannelBuffer = mutateAsThis {
    _.clear()
  }

  def withMarkReaderIndex: ChannelBuffer = mutateAsThis {
    _.markReaderIndex()
  }

  def withResetReaderIndex: ChannelBuffer = mutateAsThis {
    _.resetReaderIndex()
  }

  def withMarkWriterIndex: ChannelBuffer = mutateAsThis {
    _.markWriterIndex()
  }

  def withResetWriterIndex: ChannelBuffer = mutateAsThis {
    _.resetWriterIndex()
  }

  def withDiscardReadBytes: ChannelBuffer = mutateAsThis {
    _.discardReadBytes()
  }

  def withEnsureWritableBytes(writableBytes: Int): ChannelBuffer = mutateAsThis {
    _.ensureWritableBytes(writableBytes)
  }

  def getByte(index: Int): Byte = underlying.getByte(index)

  def getUnsignedByte(index: Int): Short = underlying.getUnsignedByte(index)

  def getShort(index: Int): Short = underlying.getShort(index)

  def getUnsignedShort(index: Int): Int = underlying.getUnsignedShort(index)

  def getMedium(index: Int): Int = underlying.getMedium(index)

  def getUnsignedMedium(index: Int): Int = underlying.getUnsignedMedium(index)

  def getInt(index: Int): Int = underlying.getInt(index)

  def getUnsignedInt(index: Int): Long = underlying.getUnsignedInt(index)

  def getLong(index: Int): Long = underlying.getLong(index)

  def getChar(index: Int): Char = underlying.getChar(index)

  def getFloat(index: Int): Float = underlying.getFloat(index)

  def getDouble(index: Int): Double = underlying.getDouble(index)

  def getBytes(index: Int): ChannelBuffer =
    getBytes(index, underlying.capacity() - (index + 1))

  def getBytes(index: Int, length: Int): ChannelBuffer = {
    val dst = ChannelBuffers.buffer(underlying.order(), length)
    underlying.getBytes(index, dst, length)
    ChannelBuffer.from(dst)
  }

  def getBytesAsByteArray(index: Int): Array[Byte] =
    getBytesAsByteArray(underlying.capacity() - (index + 1))

  def getBytesAsByteArray(index: Int, length: Int): Array[Byte] = {
    val dst = new Array[Byte](length)
    underlying.getBytes(index, dst, 0, length)
    dst
  }

  def getBytesAsByteBuffer(index: Int): ByteBuffer = {
    val dst = ByteBuffer.allocate(underlying.capacity() - (index + 1))
    underlying.getBytes(index, dst)
    dst
  }

  def withByte(index: Int, value: Int): ChannelBuffer = mutateAsThis {
    _.setByte(index, value)
  }

  def withShort(index: Int, value: Int): ChannelBuffer = mutateAsThis {
    _.setShort(index, value)
  }

  def withMedium(index: Int, value: Int): ChannelBuffer = mutateAsThis {
    _.setMedium(index, value)
  }

  def withInt(index: Int, value: Int): ChannelBuffer = mutateAsThis {
    _.setInt(index, value)
  }

  def withLong(index: Int, value: Long): ChannelBuffer = mutateAsThis {
    _.setLong(index, value)
  }

  def withChar(index: Int, value: Int): ChannelBuffer = mutateAsThis {
    _.setChar(index, value)
  }

  def withFloat(index: Int, value: Float): ChannelBuffer = mutateAsThis {
    _.setFloat(index, value)
  }

  def withDouble(index: Int, value: Double): ChannelBuffer = mutateAsThis {
    _.setDouble(index, value)
  }

  def withBytes(index: Int, src: ChannelBuffer): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src)
  }

  def withBytes(index: Int, src: ChannelBuffer, length: Int): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src, length)
  }

  def withBytes(index: Int, src: ChannelBuffer, srcIndex: Int, length: Int): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src, srcIndex, length)
  }

  def withBytes(index: Int, src: Array[Byte]): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src)
  }

  def withBytes(index: Int, src: Array[Byte], srcIndex: Int, length: Int): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src, srcIndex, length)
  }

  def withBytes(index: Int, src: ByteBuffer): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src)
  }

  def withBytes(index: Int, in: InputStream, length: Int): Try[ChannelBuffer] = Try {
    mutateAsThis {
      _.setBytes(index, in, length)
    }
  }

  def withBytes(index: Int, in: ScatteringByteChannel, length: Int): Try[ChannelBuffer] = Try {
    mutateAsThis {
      _.setBytes(index, in, length)
    }
  }

  def withZero(index: Int, length: Int): ChannelBuffer = mutateAsThis {
    _.setZero(index, length)
  }

  def readByte: Byte = mutateAsResult {
    _.readByte
  }

  def readUnsignedByte: Short = mutateAsResult {
    _.readUnsignedByte()
  }

  def readShort: Short = mutateAsResult {
    _.readShort()
  }

  def readUnsignedShort: Int = mutateAsResult {
    _.readUnsignedShort()
  }

  def readMedium: Int = mutateAsResult {
    _.readMedium()
  }

  def readUnsignedMedium: Int = mutateAsResult {
    _.readUnsignedMedium()
  }

  def readInt: Int = mutateAsResult {
    _.readInt
  }

  def readUnsignedInt: Long = mutateAsResult {
    _.readUnsignedInt
  }

  def readLong: Long = mutateAsResult {
    _.readLong
  }

  def readChar: Char = mutateAsResult {
    _.readChar
  }

  def readFloat: Float = mutateAsResult {
    _.readFloat
  }

  def readDouble: Double = mutateAsResult {
    _.readDouble
  }

  def readSlice(length: Int): ChannelBuffer = mutateAsThis {
    _.readSlice(length)
  }

  def readBytes: ChannelBuffer = readBytes(underlying.readableBytes())

  def readBytes(length: Int): ChannelBuffer = mutateAsResult {
    clonedUnderlying =>
      val dst = ChannelBuffers.buffer(underlying.order(), length)
      clonedUnderlying.readBytes(dst, length)
      ChannelBuffer.from(dst)
  }

  def readBytesAsByteArray: Array[Byte] = readBytesAsByteArray(underlying.readableBytes())

  def readBytesAsByteArray(length: Int): Array[Byte] = mutateAsResult {
    clonedUnderlying =>
      val dst = new Array[Byte](length)
      clonedUnderlying.readBytes(dst, 0, length)
      dst
  }

  def readBytesAsByteBuffer: ByteBuffer = mutateAsResult {
    clonedUnderlying =>
      val dst = ByteBuffer.allocate(underlying.readableBytes())
      clonedUnderlying.readBytes(dst)
      dst
  }

  def withSkipBytes(length: Int): ChannelBuffer = mutateAsThis {
    _.skipBytes(length)
  }

  def withWriteByte(value: Int): ChannelBuffer = mutateAsThis {
    _.writeByte(value)
  }

  def withWriteShort(value: Int): ChannelBuffer = mutateAsThis {
    _.writeShort(value)
  }

  def withWriteMedium(value: Int): ChannelBuffer = mutateAsThis {
    _.writeMedium(value)
  }

  def withWriteInt(value: Int): ChannelBuffer = mutateAsThis {
    _.writeInt(value)
  }

  def withWriteLong(value: Long): ChannelBuffer = mutateAsThis {
    _.writeLong(value)
  }

  def withWriteChar(value: Int): ChannelBuffer = mutateAsThis {
    _.writeChar(value)
  }

  def withWriteFloat(value: Float): ChannelBuffer = mutateAsThis {
    _.writeFloat(value)
  }

  def withWriteDouble(value: Double): ChannelBuffer = mutateAsThis {
    _.writeDouble(value)
  }

  def withWriteBytes(src: ChannelBuffer): ChannelBuffer = mutateAsThis {
    _.writeBytes(src)
  }

  def withWriteBytes(src: ChannelBuffer, length: Int): ChannelBuffer = mutateAsThis {
    _.writeBytes(src, length)
  }

  def withWriteBytes(src: ChannelBuffer, srcIndex: Int, length: Int): ChannelBuffer = mutateAsThis {
    _.writeBytes(src, srcIndex, length)
  }

  def withWriteBytes(src: Array[Byte]): ChannelBuffer = mutateAsThis {
    _.writeBytes(src)
  }

  def withWriteBytes(src: Array[Byte], srcIndex: Int, length: Int): ChannelBuffer = mutateAsThis {
    _.writeBytes(src, srcIndex, length)
  }

  def withWriteBytes(src: ByteBuffer): ChannelBuffer = mutateAsThis {
    _.writeBytes(src)
  }

  def withWriteBytes(in: InputStream, length: Int): ChannelBuffer = mutateAsThis {
    _.writeBytes(in, length)
  }

  def withWriteBytes(in: ScatteringByteChannel, length: Int): ChannelBuffer = mutateAsThis {
    _.writeBytes(in, length)
  }

  def withWriteZero(length: Int): ChannelBuffer = mutateAsThis {
    _.writeZero(length)
  }

  def indexOf(fromIndex: Int, toIndex: Int, value: Byte): Int = underlying.indexOf(fromIndex, toIndex, value)

  def bytesBefore(value: Byte): Int = underlying.bytesBefore(value)

  def bytesBefore(length: Int, value: Byte): Int = underlying.bytesBefore(length, value)

  def bytesBefore(index: Int, length: Int, value: Byte): Int = underlying.bytesBefore(index, length, value)

  private def internalClone = {
    val cloned = underlying.copy(0, capacity)
    val result = ChannelBufferImpl(cloned)
    cloned.resetReaderIndex()
    cloned.resetWriterIndex()
    result
  }

  def copy: ChannelBuffer = ChannelBuffer.from(underlying.copy)

  def copy(index: Int, length: Int): ChannelBuffer = ChannelBuffer.from(underlying.copy(index, length))

  def slice: ChannelBuffer = ChannelBuffer.from(underlying.slice)

  def slice(index: Int, length: Int): ChannelBuffer = ChannelBuffer.from(underlying.slice(index, length))

  def duplicate: ChannelBuffer = ChannelBuffer.from(underlying.duplicate())

  def toByteBuffer: ByteBuffer = underlying.toByteBuffer

  def toByteBuffer(index: Int, length: Int): ByteBuffer = underlying.toByteBuffer(index, length)

  def toByteBuffers: Array[ByteBuffer] = underlying.toByteBuffers

  def toByteBuffers(index: Int, length: Int): Array[ByteBuffer] = underlying.toByteBuffers(index, length)

  def hasArray: Boolean = underlying.hasArray

  def array: Array[Byte] = underlying.array()

  def arrayOffset: Int = underlying.arrayOffset()

  def toString(charset: Charset): String = underlying.toString(charset)

  def toString(index: Int, length: Int, charset: Charset): String = underlying.toString(index, length, charset)

  def compareTo(buffer: ChannelBuffer): Int = underlying.compareTo(buffer)
}
