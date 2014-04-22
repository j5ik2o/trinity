package org.sisioh.trinity.domain.io.buffer

import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.ScatteringByteChannel
import org.jboss.netty.buffer.{ChannelBuffer => NettyChannelBuffer}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer.toNetty
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer.toTrinity
import org.sisioh.trinity.domain.io.buffer.ChannelBufferFactory.toTrinity
import org.sisioh.trinity.domain.io.http.Charset
import scala.util.Try

/**
 * Represent the implementation class for [[ChannelBuffer]]
 *
 * @param underlying [[NettyChannelBuffer]]
 * @param mutated if `true` is mutable mode
 */
private[buffer]
case class ChannelBufferImpl
(underlying: NettyChannelBuffer, mutated: Boolean = false) extends ChannelBuffer {

  override def equals(obj: Any): Boolean = obj match {
    case ChannelBufferImpl(netty, _) =>
      underlying == netty
    case _ => false
  }

  override def hashCode() =
    31 * underlying.##

  override def toString() = underlying.toString

  private def mutateAsThis(f: (NettyChannelBuffer) => Unit): ChannelBuffer = {
    val result = if (mutated) this else internalClone
    f(result.underlying)
    result
  }

  private def mutateAsResult[A](f: (NettyChannelBuffer) => A): A = {
    val result = if (mutated) this else internalClone
    f(result.underlying)
  }

  override def factory: ChannelBufferFactory = underlying.factory()

  override def capacity: Int = underlying.capacity()

  override def byteOrder: ByteOrder = underlying.order()

  override def isDirect: Boolean = underlying.isDirect

  override def readerIndex: Int = underlying.readerIndex()

  override def writerIndex: Int = underlying.writerIndex()

  override def withReaderIndex(readerIndex: Int): ChannelBuffer = mutateAsThis {
    _.readerIndex(readerIndex)
  }

  override def withWriterIndex(writerIndex: Int): ChannelBuffer = mutateAsThis {
    _.writerIndex(writerIndex)
  }

  override def withIndex(readerIndex: Int, writerIndex: Int): ChannelBuffer = mutateAsThis {
    _.setIndex(readerIndex, writerIndex)
  }

  override def readableBytes: Int = underlying.readableBytes()

  override def writableBytes: Int = underlying.writableBytes()

  override def isReadable: Boolean = underlying.readable()

  override def isWritable: Boolean = underlying.writable()

  override def withClear: ChannelBuffer = mutateAsThis {
    _.clear()
  }

  override def withMarkReaderIndex: ChannelBuffer = mutateAsThis {
    _.markReaderIndex()
  }

  override def withResetReaderIndex: ChannelBuffer = mutateAsThis {
    _.resetReaderIndex()
  }

  override def withMarkWriterIndex: ChannelBuffer = mutateAsThis {
    _.markWriterIndex()
  }

  override def withResetWriterIndex: ChannelBuffer = mutateAsThis {
    _.resetWriterIndex()
  }

  override def withDiscardReadBytes: ChannelBuffer = mutateAsThis {
    _.discardReadBytes()
  }

  override def withEnsureWritableBytes(writableBytes: Int): ChannelBuffer = mutateAsThis {
    _.ensureWritableBytes(writableBytes)
  }

  override def getByte(index: Int): Byte = underlying.getByte(index)

  override def getUnsignedByte(index: Int): Short = underlying.getUnsignedByte(index)

  override def getShort(index: Int): Short = underlying.getShort(index)

  override def getUnsignedShort(index: Int): Int = underlying.getUnsignedShort(index)

  override def getMedium(index: Int): Int = underlying.getMedium(index)

  override def getUnsignedMedium(index: Int): Int = underlying.getUnsignedMedium(index)

  override def getInt(index: Int): Int = underlying.getInt(index)

  override def getUnsignedInt(index: Int): Long = underlying.getUnsignedInt(index)

  override def getLong(index: Int): Long = underlying.getLong(index)

  override def getChar(index: Int): Char = underlying.getChar(index)

  override def getFloat(index: Int): Float = underlying.getFloat(index)

  override def getDouble(index: Int): Double = underlying.getDouble(index)

  override def getBytes(index: Int): ChannelBuffer =
    getBytes(index, underlying.capacity() - (index + 1))

  override def getBytes(index: Int, length: Int): ChannelBuffer = {
    val dst = ChannelBuffers.buffer(underlying.order(), length)
    underlying.getBytes(index, dst, length)
    dst
  }

  override def getBytesAsByteArray(index: Int): Array[Byte] =
    getBytesAsByteArray(underlying.capacity() - (index + 1))

  override def getBytesAsByteArray(index: Int, length: Int): Array[Byte] = {
    val dst = new Array[Byte](length)
    underlying.getBytes(index, dst, 0, length)
    dst
  }

  override def getBytesAsByteBuffer(index: Int): ByteBuffer = {
    val dst = ByteBuffer.allocate(underlying.capacity() - (index + 1))
    underlying.getBytes(index, dst)
    dst
  }

  override def withByte(index: Int, value: Int): ChannelBuffer = mutateAsThis {
    _.setByte(index, value)
  }

  override def withShort(index: Int, value: Int): ChannelBuffer = mutateAsThis {
    _.setShort(index, value)
  }

  override def withMedium(index: Int, value: Int): ChannelBuffer = mutateAsThis {
    _.setMedium(index, value)
  }

  override def withInt(index: Int, value: Int): ChannelBuffer = mutateAsThis {
    _.setInt(index, value)
  }

  override def withLong(index: Int, value: Long): ChannelBuffer = mutateAsThis {
    _.setLong(index, value)
  }

  override def withChar(index: Int, value: Int): ChannelBuffer = mutateAsThis {
    _.setChar(index, value)
  }

  override def withFloat(index: Int, value: Float): ChannelBuffer = mutateAsThis {
    _.setFloat(index, value)
  }

  override def withDouble(index: Int, value: Double): ChannelBuffer = mutateAsThis {
    _.setDouble(index, value)
  }

  override def withBytes(index: Int, src: ChannelBuffer): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src)
  }

  override def withBytes(index: Int, src: ChannelBuffer, length: Int): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src, length)
  }

  override def withBytes(index: Int, src: ChannelBuffer, srcIndex: Int, length: Int): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src, srcIndex, length)
  }

  override def withBytes(index: Int, src: Array[Byte]): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src)
  }

  override def withBytes(index: Int, src: Array[Byte], srcIndex: Int, length: Int): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src, srcIndex, length)
  }

  override def withBytes(index: Int, src: ByteBuffer): ChannelBuffer = mutateAsThis {
    _.setBytes(index, src)
  }

  override def withBytes(index: Int, in: InputStream, length: Int): Try[ChannelBuffer] = Try {
    mutateAsThis {
      _.setBytes(index, in, length)
    }
  }

  override def withBytes(index: Int, in: ScatteringByteChannel, length: Int): Try[ChannelBuffer] = Try {
    mutateAsThis {
      _.setBytes(index, in, length)
    }
  }

  override def withZero(index: Int, length: Int): ChannelBuffer = mutateAsThis {
    _.setZero(index, length)
  }

  override def readByte: Byte = mutateAsResult {
    _.readByte
  }

  override def readUnsignedByte: Short = mutateAsResult {
    _.readUnsignedByte()
  }

  override def readShort: Short = mutateAsResult {
    _.readShort()
  }

  override def readUnsignedShort: Int = mutateAsResult {
    _.readUnsignedShort()
  }

  override def readMedium: Int = mutateAsResult {
    _.readMedium()
  }

  override def readUnsignedMedium: Int = mutateAsResult {
    _.readUnsignedMedium()
  }

  override def readInt: Int = mutateAsResult {
    _.readInt
  }

  override def readUnsignedInt: Long = mutateAsResult {
    _.readUnsignedInt
  }

  override def readLong: Long = mutateAsResult {
    _.readLong
  }

  override def readChar: Char = mutateAsResult {
    _.readChar
  }

  override def readFloat: Float = mutateAsResult {
    _.readFloat
  }

  override def readDouble: Double = mutateAsResult {
    _.readDouble
  }

  override def readBytes: ChannelBuffer = readBytes(underlying.readableBytes())

  override def readBytes(length: Int): ChannelBuffer = mutateAsResult {
    clonedUnderlying =>
      val dst = ChannelBuffers.buffer(underlying.order(), length)
      clonedUnderlying.readBytes(dst, length)
      dst
  }

  override def readBytesAsByteArray: Array[Byte] = readBytesAsByteArray(underlying.readableBytes())

  override def readBytesAsByteArray(length: Int): Array[Byte] = mutateAsResult {
    clonedUnderlying =>
      val dst = new Array[Byte](length)
      clonedUnderlying.readBytes(dst, 0, length)
      dst
  }

  override def readBytesAsByteBuffer: ByteBuffer = mutateAsResult {
    clonedUnderlying =>
      val dst = ByteBuffer.allocate(underlying.readableBytes())
      clonedUnderlying.readBytes(dst)
      dst
  }

  override def withSkipBytes(length: Int): ChannelBuffer = mutateAsThis {
    _.skipBytes(length)
  }

  override def withWriteByte(value: Int): ChannelBuffer = mutateAsThis {
    _.writeByte(value)
  }

  override def withWriteShort(value: Int): ChannelBuffer = mutateAsThis {
    _.writeShort(value)
  }

  override def withWriteMedium(value: Int): ChannelBuffer = mutateAsThis {
    _.writeMedium(value)
  }

  override def withWriteInt(value: Int): ChannelBuffer = mutateAsThis {
    _.writeInt(value)
  }

  override def withWriteLong(value: Long): ChannelBuffer = mutateAsThis {
    _.writeLong(value)
  }

  override def withWriteChar(value: Int): ChannelBuffer = mutateAsThis {
    _.writeChar(value)
  }

  override def withWriteFloat(value: Float): ChannelBuffer = mutateAsThis {
    _.writeFloat(value)
  }

  override def withWriteDouble(value: Double): ChannelBuffer = mutateAsThis {
    _.writeDouble(value)
  }

  override def withWriteBytes(src: ChannelBuffer): ChannelBuffer = mutateAsThis {
    _.writeBytes(src)
  }

  override def withWriteBytes(src: ChannelBuffer, length: Int): ChannelBuffer = mutateAsThis {
    _.writeBytes(src, length)
  }

  override def withWriteBytes(src: ChannelBuffer, srcIndex: Int, length: Int): ChannelBuffer = mutateAsThis {
    _.writeBytes(src, srcIndex, length)
  }

  override def withWriteBytes(src: Array[Byte]): ChannelBuffer = mutateAsThis {
    _.writeBytes(src)
  }

  override def withWriteBytes(src: Array[Byte], srcIndex: Int, length: Int): ChannelBuffer = mutateAsThis {
    _.writeBytes(src, srcIndex, length)
  }

  override def withWriteBytes(src: ByteBuffer): ChannelBuffer = mutateAsThis {
    _.writeBytes(src)
  }

  override def withWriteBytes(in: InputStream, length: Int): ChannelBuffer = mutateAsThis {
    _.writeBytes(in, length)
  }

  override def withWriteBytes(in: ScatteringByteChannel, length: Int): ChannelBuffer = mutateAsThis {
    _.writeBytes(in, length)
  }

  override def withWriteZero(length: Int): ChannelBuffer = mutateAsThis {
    _.writeZero(length)
  }

  override def indexOf(fromIndex: Int, toIndex: Int, value: Byte): Int = underlying.indexOf(fromIndex, toIndex, value)

  override def bytesBefore(value: Byte): Int = underlying.bytesBefore(value)

  override def bytesBefore(length: Int, value: Byte): Int = underlying.bytesBefore(length, value)

  override def bytesBefore(index: Int, length: Int, value: Byte): Int = underlying.bytesBefore(index, length, value)

  private def internalClone = {
    val cloned = underlying.copy(0, capacity)
    val result = ChannelBufferImpl(cloned)
    cloned.resetReaderIndex()
    cloned.resetWriterIndex()
    result
  }

  override def copy: ChannelBuffer = underlying.copy

  override def copy(index: Int, length: Int): ChannelBuffer = underlying.copy(index, length)

  override def slice: ChannelBuffer = underlying.slice

  override def slice(index: Int, length: Int): ChannelBuffer = underlying.slice(index, length)

  override def duplicate: ChannelBuffer = underlying.duplicate()

  override def toByteBuffer: ByteBuffer = underlying.toByteBuffer

  override def toByteBuffer(index: Int, length: Int): ByteBuffer = underlying.toByteBuffer(index, length)

  override def toByteBuffers: Array[ByteBuffer] = underlying.toByteBuffers

  override def toByteBuffers(index: Int, length: Int): Array[ByteBuffer] = underlying.toByteBuffers(index, length)

  override def hasArray: Boolean = underlying.hasArray

  override def array: Array[Byte] = underlying.array()

  override def arrayOffset: Int = underlying.arrayOffset()

  override def toString(charset: Charset): String = underlying.toString(charset.toObject)

  override def toString(index: Int, length: Int, charset: Charset): String = underlying.toString(index, length, charset.toObject)

  override def compareTo(buffer: ChannelBuffer): Int = underlying.compareTo(buffer)

}
