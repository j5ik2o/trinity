package org.sisioh.trinity.domain.mvc.http

import java.io.FileOutputStream
import org.jboss.netty.buffer.{ChannelBuffer => NettyChannelBuffer}
import org.jboss.netty.handler.codec.http.multipart.MixedFileUpload
import org.sisioh.scala.toolbox.Loan.using
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer.toNetty
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer.toTrinity
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.future


/**
 * マルチパートアイテムを表現する値オブジェクト。
 *
 * @param mixedFileUpload [[org.jboss.netty.handler.codec.http.multipart.MixedFileUpload]]
 */
case class MultiPartItem(mixedFileUpload: MixedFileUpload, ioChunkSize: Int = 1024) {

  val data: ChannelBuffer = mixedFileUpload.getChannelBuffer

  val name = mixedFileUpload.getName

  val contentType = mixedFileUpload.getContentType

  val fileName = mixedFileUpload.getFilename

  def writeToFile(path: String)
                 (implicit executor: ExecutionContext): Future[Unit] = future {
    using(new FileOutputStream(path)) {
      fis =>
        val netty: NettyChannelBuffer = data
        while (netty.readable()) {
          netty.readBytes(fis, ioChunkSize)
        }
    }.get
  }

}


