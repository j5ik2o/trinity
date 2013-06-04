package org.sisioh.trinity

import java.io._
import org.jboss.netty.handler.codec.http.multipart.MixedFileUpload
import org.sisioh.scala.toolbox.Loan._

case class MultipartItem(mixedFileUpload: MixedFileUpload) {

  val data = mixedFileUpload.get
  val name = mixedFileUpload.getName
  val contentType = mixedFileUpload.getContentType
  val fileName = mixedFileUpload.getFilename

  def writeToFile(path: String) = using(new FileOutputStream(path)) {
    _.write(data)
  }

}
