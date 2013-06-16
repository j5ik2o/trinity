package org.sisioh.trinity.domain

import com.twitter.finagle.http.{Request => FinagleRequest}
import java.io._
import org.jboss.netty.handler.codec.http.multipart.{MixedFileUpload, HttpPostRequestDecoder}
import org.sisioh.scala.toolbox.Loan._
import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import org.sisioh.trinity.domain

/**
 * マルチパートアイテムを表現する値オブジェクト。
 *
 * @param mixedFileUpload [[org.jboss.netty.handler.codec.http.multipart.MixedFileUpload]]
 */
case class MultiPartItem(mixedFileUpload: MixedFileUpload) {

  val data = mixedFileUpload.get
  val name = mixedFileUpload.getName
  val contentType = mixedFileUpload.getContentType
  val fileName = mixedFileUpload.getFilename

  def writeToFile(path: String) = future {
    using(new FileOutputStream(path)) {
      _.write(data)
    }.get
  }

}

object MultiPartItem {

  /**
   * リクエストから[[domain.MultiPartItem]]のマップを取得する。
   *
   * @param request [[com.twitter.finagle.http.Request]]
   * @return [[domain.MultiPartItem]]のマップ
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
