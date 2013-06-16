package org.sisioh.trinity

import java.io._
import org.sisioh.scala.toolbox.Loan._
import scala.collection.JavaConversions._
import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.multipart.{MixedFileUpload, HttpPostRequestDecoder}
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

case class MultipartItem(mixedFileUpload: MixedFileUpload) {

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

object MultipartItem {

  def fromRequest(request: FinagleRequest): Map[String, MultipartItem] = {
    val dec = new HttpPostRequestDecoder(request)
    val r: Map[String, MultipartItem] = if (dec.isMultipart) {
      dec.getBodyHttpDatas.map {
        data =>
          val mpi = MultipartItem(data.asInstanceOf[MixedFileUpload])
          (data.getName -> mpi)
      }.toMap
    } else Map.empty[String, MultipartItem]
    r
  }

}
