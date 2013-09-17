package org.sisioh.trinity.domain.io.buffer.mutable

import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

abstract class AbstractChannelBuffer extends ChannelBuffer with Cloneable {

  private var _readerIndex = 0

  private var _writerIndex = 0

  def readerIndex: Int = _readerIndex

  def writerIndex: Int = _writerIndex

  def withReaderIndex(readerIndex: Int): ChannelBuffer = {
    _readerIndex = readerIndex
    this
  }

  def withWriterIndex(writerIndex: Int): ChannelBuffer = {
    _writerIndex = writerIndex
    this
  }

  def withIndex(readerIndex: Int, writerIndex: Int): ChannelBuffer =
    withReaderIndex(readerIndex).withWriterIndex(writerIndex)

  /**
   * クローンを生成する。
   *
   * @return クローンしたインスタンス
   */
  override def clone: ChannelBuffer =
    super.clone.asInstanceOf[ChannelBuffer]

}
