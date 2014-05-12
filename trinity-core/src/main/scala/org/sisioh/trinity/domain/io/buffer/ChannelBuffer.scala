package org.sisioh.trinity.domain.io.buffer

import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.ScatteringByteChannel
import org.jboss.netty.buffer.{ChannelBuffer => NettyChannelBuffer}
import org.jboss.netty.buffer.{ChannelBuffers => NettyChannelBuffers}
import org.sisioh.trinity.domain.io.http.Charset
import scala.language.implicitConversions
import scala.util.Try

/**
 * Represents a trait for `org.jboss.netty.buffer.ChannelBuffer`.
 */
trait ChannelBuffer {

  /**
   * Gets [[ChannelBufferFactory]].
   *
   * @return [[ChannelBufferFactory]]
   */
  def factory: ChannelBufferFactory

  /**
   * Gets a capacity.
   *
   * @return capacity
   */
  def capacity: Int

  /**
   * Gets [[ByteOrder]].
   *
   * @return [[ByteOrder]]
   */
  def byteOrder: ByteOrder

  /**
   * Gets whether direct.
   *
   * @return direct
   */
  def isDirect: Boolean

  /**
   * Gets reader's index.
   *
   * @return reader's index.
   */
  def readerIndex: Int

  /**
   * Gets writer's index.
   *
   * @return writer's index
   */
  def writerIndex: Int

  /**
   * Creates a new instance with a reader's index.
   *
   * @param readerIndex reader's index
   * @return a new instance
   */
  def withReaderIndex(readerIndex: Int): ChannelBuffer

  /**
   * Creates a new instance with a writer's index.
   *
   * @param writerIndex writer's index
   * @return a new instance
   */
  def withWriterIndex(writerIndex: Int): ChannelBuffer

  /**
   * Creates a new instance with a indexes.
   *
   * @param readerIndex reader's index
   * @param writerIndex writer's index
   * @return a new instance
   */
  def withIndex(readerIndex: Int, writerIndex: Int): ChannelBuffer

  /**
   * Gets a readable bytes size.
   *
   * @return readable bytes size
   */
  def readableBytes: Int

  /**
   * Gets a writable bytes size.
   *
   * @return writable bytes size
   */
  def writableBytes: Int

  /**
   * Gets whether readable.
   *
   * @return true if it's readable.
   */
  def isReadable: Boolean

  /**
   * Gets whether writable.
   *
   * @return true if it's writable.
   */
  def isWritable: Boolean

  /**
   * Creates a new instance that was cleared.
   *
   * @return a new instance
   */
  def withClear: ChannelBuffer

  /**
   * Creates a new instance that was marked reader's index.
   *
   * @return a new instance
   */
  def withMarkReaderIndex: ChannelBuffer

  /**
   * Creates a new instance that was reset reader's index.
   *
   * @return a new instance
   */
  def withResetReaderIndex: ChannelBuffer

  /**
   * Creates a new instance that was marked writer's index.
   *
   * @return a new instance
   */
  def withMarkWriterIndex: ChannelBuffer

  /**
   * Creates a new instance that was reset writer's index.
   *
   * @return a new instance
   */
  def withResetWriterIndex: ChannelBuffer

  /**
   * Creates a new instance that was discarded read bytes.
   *
   * @return a new instance
   */
  def withDiscardReadBytes: ChannelBuffer

  /**
   * Creates a new instance that was ensured writable bytes.
   *
   * @param writableBytes
   * @return a new instance
   */
  def withEnsureWritableBytes(writableBytes: Int): ChannelBuffer

  // --- Reads by using absolute position.

  /**
   * Gets a byte(8-bit) data at the specified absolute index in this buffer.
   *
   * @param index
   * @return data
   */
  def getByte(index: Int): Byte

  /**
   * Gets an unsigned byte(8-bit) data at the specified absolute index in this buffer.
   *
   * @param index index
   * @return data
   */
  def getUnsignedByte(index: Int): Short

  /**
   * Gets a short(16-bit) data at the specified absolute index in this buffer.
   *
   * @param index
   * @return data
   */
  def getShort(index: Int): Short

  /**
   * Gets an unsigned short(16-bit) data at the absolute index in this buffer.
   *
   * @param index
   * @return data
   */
  def getUnsignedShort(index: Int): Int

  /**
   * Gets a medium(24-bit) data at the absolute index in this buffer.
   *
   * @param index
   * @return data
   */
  def getMedium(index: Int): Int

  /**
   * Gets an unsigned medium(24-bit) data at the absolute index in this buffer.
   *
   * @param index index
   * @return data
   */
  def getUnsignedMedium(index: Int): Int

  /**
   * Gets an unsigned int(32-bit) data at the absolute index in this buffer.
   *
   * @param index index
   * @return data
   */
  def getInt(index: Int): Int

  /**
   * Gets a int(32-bit) data at the absolute index in this buffer.
   *
   * @param index
   * @return data
   */
  def getUnsignedInt(index: Int): Long

  /**
   * Gets a long(64-bit) data at the absolute index in this buffer.
   *
   * @param index
   * @return data
   */
  def getLong(index: Int): Long

  /**
   * Gets a char(2-byte UTF-16) data at the absolute index in this buffer.
   *
   * @param index
   * @return data
   */
  def getChar(index: Int): Char

  /**
   * Gets a float(32-bit) data at the absolute index in this buffer.
   *
   * @param index index
   * @return data
   */
  def getFloat(index: Int): Float

  /**
   * Gets a double(64-bit) data at the absolute index in this buffer.
   *
   * @param index index
   * @return data
   */
  def getDouble(index: Int): Double

  /**
   * Gets a [[ChannelBuffer]] at the absolute index in this buffer.
   *
   * @param index index
   * @return data
   */
  def getBytes(index: Int): ChannelBuffer

  /**
   * Gets a [[ChannelBuffer]] at the absolute index in this. buffer.
   *
   * @param index
   * @param length
   * @return data
   */
  def getBytes(index: Int, length: Int): ChannelBuffer

  /**
   * Gets a Array of Byte at the absolute index in this buffer.
   *
   * @param index
   * @return data
   */
  def getBytesAsByteArray(index: Int): Array[Byte]

  /**
   * Gets a Array of Byte at the absolute index in this buffer.
   *
   * @param index
   * @param length
   * @return data
   */
  def getBytesAsByteArray(index: Int, length: Int): Array[Byte]

  /**
   * Gets a ByteBuffer at the absolute index in this buffer.
   *
   * @param index
   * @return data
   */
  def getBytesAsByteBuffer(index: Int): ByteBuffer

  // --- Writes by using absolute position.

  /**
   * Creates a new instance or gets this that was written a byte(8-bit) value.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param value
   * @return a new instance or this
   */
  def withByte(index: Int, value: Int): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a short(16-bit) value.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param value
   * @return a new instance or this
   */
  def withShort(index: Int, value: Int): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a medium(24-bit) value.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param value
   * @return a new instance or this
   */
  def withMedium(index: Int, value: Int): ChannelBuffer

  /**
   * Creates a new instance gets this that was written a int(32-bit) value.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param value
   * @return a new instance or this
   */
  def withInt(index: Int, value: Int): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a long(64-bit) value.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param value
   * @return a new instance or this
   */
  def withLong(index: Int, value: Long): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a char(2-byte UTF-16) value.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param value
   * @return a new instance or this
   */
  def withChar(index: Int, value: Int): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a float(32-bit) value.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param value
   * @return a new instance or this
   */
  def withFloat(index: Int, value: Float): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a double(64-bit) value.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param value
   * @return a new instance or this
   */
  def withDouble(index: Int, value: Double): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a [[ChannelBuffer]].
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param src [[ChannelBuffer]]
   * @return a new instance or this
   */
  def withBytes(index: Int, src: ChannelBuffer): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a [[ChannelBuffer]].
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param src [[ChannelBuffer]]
   * @param length write data length
   * @return a new instance or this
   */
  def withBytes(index: Int, src: ChannelBuffer, length: Int): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a [[ChannelBuffer]].
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param src [[ChannelBuffer]]
   * @param srcIndex absolute index in the source buffer
   * @param length write data length
   * @return a new instance or this
   */
  def withBytes(index: Int, src: ChannelBuffer, srcIndex: Int, length: Int): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a Byte Array.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param src [[Array]] of [[Byte]]
   * @return a new instance or this
   */
  def withBytes(index: Int, src: Array[Byte]): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a Byte Array.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param src [[Array]] of [[Byte]]
   * @param srcIndex absolute index in the source buffer
   * @param length write data length
   * @return a new instance or this
   */
  def withBytes(index: Int, src: Array[Byte], srcIndex: Int, length: Int): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a [[ByteBuffer]].
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param src [[ByteBuffer]]
   * @return a new instance or this
   */
  def withBytes(index: Int, src: ByteBuffer): ChannelBuffer

  /**
   * Creates a new instance or gets this that was written a input stream.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param in [[InputStream]]
   * @param length write data length
   * @return a new instance or this
   */
  def withBytes(index: Int, in: InputStream, length: Int): Try[ChannelBuffer]

  /**
   * Creates a new instance or gets this that was written a [[ScatteringByteChannel]].
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param in [[ScatteringByteChannel]]
   * @param length write data length
   * @return a new instance or this
   */
  def withBytes(index: Int, in: ScatteringByteChannel, length: Int): Try[ChannelBuffer]

  /**
   * Creates a new instance or gets this that was filled it with zero value.
   * if this buffer is immutable, then returns a new instance, else returns this.
   *
   * @param index
   * @param length
   * @return a new instance or this
   */
  def withZero(index: Int, length: Int): ChannelBuffer

  // --- Reads by using relative position.

  /**
   * Reads a byte(8-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readByte: Byte

  /**
   * Reads an unsigned byte(8-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readUnsignedByte: Short

  /**
   * Reads an short(24-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readShort: Short

  /**
   * Reads an unsigned short(16-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readUnsignedShort: Int

  /**
   * Reads a medium(24-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readMedium: Int

  /**
   * Reads an unsigned medium(24-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readUnsignedMedium: Int

  /**
   * Reads a int(32-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readInt: Int

  /**
   * Reads an unsigned int(32-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readUnsignedInt: Long

  /**
   * Reads a long(64-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readLong: Long

  /**
   * Reads a char(2-bytes UTF-16) data at the relative index in the buffer.
   *
   * @return data
   */
  def readChar: Char

  /**
   * Reads a float(32-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readFloat: Float

  /**
   * Reads a double(64-bit) data at the relative index in the buffer.
   *
   * @return data
   */
  def readDouble: Double

  /**
   * Reads a [[ChannelBuffer]] at the relative index in the buffer.
   *
   * @return [[ChannelBuffer]]
   */
  def readBytes: ChannelBuffer

  /**
   * Reads a [[ChannelBuffer]] at the relative index in the buffer.
   *
   * @param length read data length
   * @return data
   */
  def readBytes(length: Int): ChannelBuffer

  def readBytesAsByteArray: Array[Byte]

  def readBytesAsByteArray(length: Int): Array[Byte]

  def readBytesAsByteBuffer: ByteBuffer

  // --- Writes by using relative position.

  def withSkipBytes(length: Int): ChannelBuffer

  def withWriteByte(value: Int): ChannelBuffer

  def withWriteShort(value: Int): ChannelBuffer

  def withWriteMedium(value: Int): ChannelBuffer

  def withWriteInt(value: Int): ChannelBuffer

  def withWriteLong(value: Long): ChannelBuffer

  def withWriteChar(value: Int): ChannelBuffer

  def withWriteFloat(value: Float): ChannelBuffer

  def withWriteDouble(value: Double): ChannelBuffer

  def withWriteBytes(src: ChannelBuffer): ChannelBuffer

  def withWriteBytes(src: ChannelBuffer, length: Int): ChannelBuffer

  def withWriteBytes(src: ChannelBuffer, srcIndex: Int, length: Int): ChannelBuffer

  def withWriteBytes(src: Array[Byte]): ChannelBuffer

  def withWriteBytes(src: Array[Byte], srcIndex: Int, length: Int): ChannelBuffer

  def withWriteBytes(src: ByteBuffer): ChannelBuffer

  def withWriteBytes(in: InputStream, length: Int): ChannelBuffer

  def withWriteBytes(in: ScatteringByteChannel, length: Int): ChannelBuffer

  def withWriteZero(length: Int): ChannelBuffer

  def indexOf(fromIndex: Int, toIndex: Int, value: Byte): Int

  //  def indexOf(fromIndex: Int, toIndex: Int, indexFinder: ChannelBufferIndexFinder): Int

  def bytesBefore(value: Byte): Int

  //  def bytesBefore(indexFinder: ChannelBufferIndexFinder): Int

  def bytesBefore(length: Int, value: Byte): Int

  //  def bytesBefore(length: Int, indexFinder: ChannelBufferIndexFinder): Int

  def bytesBefore(index: Int, length: Int, value: Byte): Int

  //  def bytesBefore(index: Int, length: Int, indexFinder: ChannelBufferIndexFinder): Int

  def copy: ChannelBuffer

  def copy(index: Int, length: Int): ChannelBuffer

  def slice: ChannelBuffer

  def slice(index: Int, length: Int): ChannelBuffer

  def duplicate: ChannelBuffer

  def toByteBuffer: ByteBuffer

  def toByteBuffer(index: Int, length: Int): ByteBuffer

  def toByteBuffers: Array[ByteBuffer]

  def toByteBuffers(index: Int, length: Int): Array[ByteBuffer]

  def hasArray: Boolean

  def array: Array[Byte]

  def arrayOffset: Int

  def toString(charset: Charset): String

  def toString(index: Int, length: Int, charset: Charset): String

  def compareTo(buffer: ChannelBuffer): Int

}

/**
 * A companion object for [[ChannelBuffer]]
 */
object ChannelBuffer {

  val empty: ChannelBuffer = NettyChannelBuffers.EMPTY_BUFFER

  private[trinity] implicit def toNetty(target: ChannelBuffer): NettyChannelBuffer =
    target match {
      case ChannelBufferImpl(underlying, _) => underlying
      case _ => throw new IllegalArgumentException()
    }

  private[trinity] implicit def toTrinity(underlying: NettyChannelBuffer): ChannelBuffer = ChannelBufferImpl(underlying)

}