/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
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
package org.sisioh.trinity.domain.fileupload

import com.twitter.finagle.http.{Request => FinagleRequest}
import java.io._
import org.jboss.netty.handler.codec.http.multipart.{MixedFileUpload, HttpPostRequestDecoder}
import org.sisioh.scala.toolbox.Loan._
import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

/**
 * マルチパートアイテムを表現する値オブジェクト。
 *
 * @param mixedFileUpload [[org.jboss.netty.handler.codec.http.multipart.MixedFileUpload]]
 */
case class MultiPartItem(mixedFileUpload: MixedFileUpload, ioChunkSize: Int = 1024) {

  val data = mixedFileUpload.getChannelBuffer
  val name = mixedFileUpload.getName
  val contentType = mixedFileUpload.getContentType
  val fileName = mixedFileUpload.getFilename

  def writeToFile(path: String) = future {
    using(new FileOutputStream(path)) {
      fis =>
        while (data.readable()) {
          data.readBytes(fis, ioChunkSize)
        }
    }.get
  }

}

/**
 * コンパニオンオブジェクト。
 */
object MultiPartItem {

  /**
   * リクエストから[[org.sisioh.trinity.domain.fileupload.MultiPartItem]]のマップを取得する。
   *
   * @param request `com.twitter.finagle.http.Request`
   * @return [[org.sisioh.trinity.domain.fileupload.MultiPartItem]]のマップ
   */
  def fromRequest(request: FinagleRequest): Map[String, MultiPartItem] = {
    val httpPostRequestDecoder = new HttpPostRequestDecoder(request)
    if (httpPostRequestDecoder.isMultipart) {
      httpPostRequestDecoder.getBodyHttpDatas.map {
        data =>
          data.getName -> MultiPartItem(data.asInstanceOf[MixedFileUpload])
      }.toMap
    } else Map.empty[String, MultiPartItem]
  }

}
