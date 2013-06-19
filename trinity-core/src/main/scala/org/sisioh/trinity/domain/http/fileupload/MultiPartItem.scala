package org.sisioh.trinity.domain.fileupload

import com.twitter.finagle.http.{Request => FinagleRequest}
import java.io._
import org.jboss.netty.handler.codec.http.multipart.{MixedFileUpload, HttpPostRequestDecoder}
import org.sisioh.scala.toolbox.Loan._
import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import org.sisioh.trinity.domain.fileupload

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

object MultiPartItem {

  /**
   * リクエストから[[fileupload.MultiPartItem]]のマップを取得する。
   *
   * @param request [[com.twitter.finagle.http.Request]]
   * @return [[fileupload.MultiPartItem]]のマップ
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
