package org.sisioh.trinity.domain.io.buffer.mutable

import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

/**
 * Represents the abstract class for [[ChannelBuffer]].
 */
abstract class AbstractChannelBuffer extends ChannelBuffer with Cloneable {

  private var _readerIndex = 0

  private var _writerIndex = 0

  override def readerIndex: Int = _readerIndex

  override def writerIndex: Int = _writerIndex

  override def withReaderIndex(readerIndex: Int): ChannelBuffer = {
    _readerIndex = readerIndex
    this
  }

  override def withWriterIndex(writerIndex: Int): ChannelBuffer = {
    _writerIndex = writerIndex
    this
  }

  override def withIndex(readerIndex: Int, writerIndex: Int): ChannelBuffer =
    withReaderIndex(readerIndex).withWriterIndex(writerIndex)

  override def clone: ChannelBuffer =
    super.clone.asInstanceOf[ChannelBuffer]

}
