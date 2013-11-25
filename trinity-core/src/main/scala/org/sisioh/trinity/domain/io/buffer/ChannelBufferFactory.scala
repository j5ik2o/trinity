package org.sisioh.trinity.domain.io.buffer

import java.nio.ByteBuffer
import java.nio.ByteOrder

import scala.language.implicitConversions

import org.jboss.netty.buffer.{ChannelBufferFactory => NettyChannelBufferFactory}

/**
 * [[org.sisioh.trinity.domain.io.buffer.ChannelBuffer]]のためのファクトリ。
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

  implicit def toNetty(self: ChannelBufferFactory): NettyChannelBufferFactory =
    self match {
      case ChannelBufferFactoryImpl(underlying) => underlying
      case _ => throw new IllegalArgumentException()
    }

  implicit def toTrinity(underlying: NettyChannelBufferFactory): ChannelBufferFactory =
    ChannelBufferFactoryImpl(underlying)

}