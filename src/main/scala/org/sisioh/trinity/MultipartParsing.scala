package org.sisioh.trinity

import scala.collection.mutable.Map
import scala.collection.JavaConversions._
import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.multipart.{MixedFileUpload, HttpPostRequestDecoder}

object MultipartParsing {

  def apply(request: FinagleRequest) = {
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
