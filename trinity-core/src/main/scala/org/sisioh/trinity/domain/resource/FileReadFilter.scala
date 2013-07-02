package org.sisioh.trinity.domain.resource

import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import java.io.{FileInputStream, File, InputStream}
import org.apache.commons.io.IOUtils
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.config.{Environment, Config}
import org.sisioh.trinity.domain.http.ContentType

object FileResolver {

  def apply(config: Config) =
    new FileResolver(config)

}

class FileResolver(config: Config) {

  def hasFile(path: String): Boolean = {
    if (config.environment == Environment.Product) {
      hasResourceFile(path)
    } else {
      hasLocalFile(path)
    }
  }

  def getInputStream(path: String): InputStream = {
    if (config.environment == Environment.Product) {
      getResourceInputStream(path)
    } else {
      getLocalInputStream(path)
    }
  }

  private def getResourceInputStream(path: String): InputStream = {
    getClass.getResourceAsStream(path)
  }

  private def getLocalInputStream(path: String): InputStream = {
    val file = new File(config.localDocumentRoot, path)
    new FileInputStream(file)
  }

  private def hasResourceFile(path: String): Boolean = {
    val fi = getClass.getResourceAsStream(path)
    try {
      if (fi != null && fi.available > 0) {
        true
      } else {
        false
      }
    } catch {
      case e: Exception =>
        false
    }
  }

  private def hasLocalFile(path: String): Boolean = {
    val file = new File(config.localDocumentRoot, path)
    if (file.toString.contains("trinity-core/src/test")) false
    else if (!file.exists || file.isDirectory) false
    else if (!file.canRead) false
    else true
  }

}


object FileReadFilter {

  def apply(config: Config) =
    new FileReadFilter(config)


}

class FileReadFilter(config: Config)
  extends SimpleFilter[FinagleRequest, FinagleResponse] with LoggingEx {

  val fileResolver = FileResolver(config)

  def isValidPath(path: String): Boolean = {
    try {
      val fi = getClass.getResourceAsStream(path)
      if (fi != null && fi.available > 0) true else false
    } catch {
      case e: Exception =>
        false
    }
  }

  def apply(request: FinagleRequest, service: Service[FinagleRequest, FinagleResponse]) = {
    if (fileResolver.hasFile(request.uri) && request.uri != '/') {
      val fh = fileResolver.getInputStream(request.uri)
      val b = IOUtils.toByteArray(fh)
      fh.read(b)
      val response = request.response
      val mtype = ContentType.getContentType('.' + request.uri.toString.split('.').last)
      response.status = OK
      response.setHeader("Content-Type", mtype)
      response.setContent(copiedBuffer(b))
      Future.value(response)
    } else {
      service(request)
    }
  }
}
