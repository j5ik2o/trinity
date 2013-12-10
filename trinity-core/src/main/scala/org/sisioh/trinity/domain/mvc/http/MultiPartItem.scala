/*
 * Copyright 2013 Sisioh Project and others. (http://sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.trinity.domain.mvc.http

import java.io.FileOutputStream
import org.jboss.netty.buffer.{ChannelBuffer => NettyChannelBuffer}
import org.jboss.netty.handler.codec.http.multipart.MixedFileUpload
import org.sisioh.scala.toolbox.Loan.using
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer.toNetty
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer.toTrinity
import scala.concurrent._

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


