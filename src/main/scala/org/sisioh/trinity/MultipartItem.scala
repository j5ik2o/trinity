package org.sisioh.trinity

import java.io._
import org.jboss.netty.handler.codec.http.multipart.MixedFileUpload
import org.sisioh.scala.toolbox.Loan._
import scala.collection.mutable.Map
import scala.collection.JavaConversions._
import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.multipart.{MixedFileUpload, HttpPostRequestDecoder}

case class MultipartItem(mixedFileUpload: MixedFileUpload) {

  val data = mixedFileUpload.get
  val name = mixedFileUpload.getName
  val contentType = mixedFileUpload.getContentType
  val fileName = mixedFileUpload.getFilename

  def writeToFile(path: String) = using(new FileOutputStream(path)) {
    _.write(data)
  }

}

object MultipartItem {

  def fromRequest(request: FinagleRequest): Map[String, MultipartItem] = {
    var multiParams = Map[String, MultipartItem]()
    val dec = new HttpPostRequestDecoder(request)
    if (dec.isMultipart) {
      dec.getBodyHttpDatas.foreach {
        data =>
          val mpi = new MultipartItem(data.asInstanceOf[MixedFileUpload])
          multiParams += (data.getName -> mpi)
      }
    }
    multiParams
  }

}
