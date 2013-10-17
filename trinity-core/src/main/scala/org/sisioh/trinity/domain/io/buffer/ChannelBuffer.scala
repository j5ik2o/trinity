package org.sisioh.trinity.domain.io.buffer

import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.ScatteringByteChannel
import java.nio.charset.Charset

import scala.language.implicitConversions
import scala.util.Try

import org.jboss.netty.buffer.{ChannelBuffer => NettyChannelBuffer}
import org.jboss.netty.buffer.{ChannelBuffers => NettyChannelBuffers}
import org.sisioh.trinity.domain.io.infrastructure.buffer.ChannelBufferImpl

trait ChannelBuffer {

  def factory: ChannelBufferFactory

  def capacity: Int

  def byteOrder: ByteOrder

  def isDirect: Boolean

  def readerIndex: Int

  def writerIndex: Int

  def withReaderIndex(readerIndex: Int): ChannelBuffer

  def withWriterIndex(writerIndex: Int): ChannelBuffer

  def withIndex(readerIndex: Int, writerIndex: Int): ChannelBuffer

  def readableBytes: Int

  def writableBytes: Int

  def isReadable: Boolean

  def isWritable: Boolean

  def withClear: ChannelBuffer

  def withMarkReaderIndex: ChannelBuffer

  def withResetReaderIndex: ChannelBuffer

  def withMarkWriterIndex: ChannelBuffer

  def withResetWriterIndex: ChannelBuffer

  def withDiscardReadBytes: ChannelBuffer

  def withEnsureWritableBytes(writableBytes: Int): ChannelBuffer

  // --- 絶対位置読み込み

  def getByte(index: Int): Byte

  def getUnsignedByte(index: Int): Short

  def getShort(index: Int): Short

  def getUnsignedShort(index: Int): Int

  def getMedium(index: Int): Int

  def getUnsignedMedium(index: Int): Int

  def getInt(index: Int): Int

  def getUnsignedInt(index: Int): Long

  def getLong(index: Int): Long

  def getChar(index: Int): Char

  def getFloat(index: Int): Float

  def getDouble(index: Int): Double

  def getBytes(index: Int): ChannelBuffer

  def getBytes(index: Int, length: Int): ChannelBuffer

  def getBytesAsByteArray(index: Int): Array[Byte]

  def getBytesAsByteArray(index: Int, length: Int): Array[Byte]

  def getBytesAsByteBuffer(index: Int): ByteBuffer

  // --- 絶対位置書き込み

  def withByte(index: Int, value: Int): ChannelBuffer

  def withShort(index: Int, value: Int): ChannelBuffer

  def withMedium(index: Int, value: Int): ChannelBuffer

  def withInt(index: Int, value: Int): ChannelBuffer

  def withLong(index: Int, value: Long): ChannelBuffer

  def withChar(index: Int, value: Int): ChannelBuffer

  def withFloat(index: Int, value: Float): ChannelBuffer

  def withDouble(index: Int, value: Double): ChannelBuffer

  def withBytes(index: Int, src: ChannelBuffer): ChannelBuffer

  def withBytes(index: Int, src: ChannelBuffer, length: Int): ChannelBuffer

  def withBytes(index: Int, src: ChannelBuffer, srcIndex: Int, length: Int): ChannelBuffer

  def withBytes(index: Int, src: Array[Byte]): ChannelBuffer

  def withBytes(index: Int, src: Array[Byte], srcIndex: Int, length: Int): ChannelBuffer

  def withBytes(index: Int, src: ByteBuffer): ChannelBuffer

  def withBytes(index: Int, in: InputStream, length: Int): Try[ChannelBuffer]

  def withBytes(index: Int, in: ScatteringByteChannel, length: Int): Try[ChannelBuffer]

  def withZero(index: Int, length: Int): ChannelBuffer

  // --- 相対位置読み込み

  def readByte: Byte

  def readUnsignedByte: Short

  def readShort: Short

  def readUnsignedShort: Int

  def readMedium: Int

  def readUnsignedMedium: Int

  def readInt: Int

  def readUnsignedInt: Long

  def readLong: Long

  def readChar: Char

  def readFloat: Float

  def readDouble: Double

  def readSlice(length: Int): ChannelBuffer

  def readBytes: ChannelBuffer

  def readBytes(length: Int): ChannelBuffer

  def readBytesAsByteArray: Array[Byte]

  def readBytesAsByteArray(length: Int): Array[Byte]

  def readBytesAsByteBuffer: ByteBuffer

  // --- 相対位置書き込み

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


object ChannelBuffer {

  val empty: ChannelBuffer = NettyChannelBuffers.EMPTY_BUFFER

  implicit def toNetty(target: ChannelBuffer): NettyChannelBuffer =
    target match {
      case ChannelBufferImpl(underlying, _) => underlying
      case _ => throw new IllegalArgumentException()
    }


  implicit def toTrinity(underlying: NettyChannelBuffer): ChannelBuffer = ChannelBufferImpl(underlying)

}