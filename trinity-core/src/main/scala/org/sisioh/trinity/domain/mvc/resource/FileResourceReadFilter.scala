package org.sisioh.trinity.domain.mvc.resource

import java.io.File
import org.apache.commons.io.IOUtils
import org.sisioh.trinity.domain.io.buffer.ChannelBuffers
import org.sisioh.trinity.domain.io.http.MimeTypes
import org.sisioh.trinity.domain.mvc.Environment
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.filter.SimpleFilter
import org.sisioh.trinity.domain.mvc.http.{ResponseSupport, Request, Response}
import scala.concurrent.Future
import scala.util.Try

class FileResourceReadFilter(environment: Environment.Value, localPath: File)
  extends SimpleFilter[Request, Response] with ResponseSupport {

  private val fileResolver = FileResourceResolver(environment, localPath)

  def isValidPath(path: String): Boolean = {
    try {
      val fi = getClass.getResourceAsStream(path)
      if (fi != null && fi.available > 0) true else false
    } catch {
      case e: Exception =>
        false
    }
  }


  def apply(requestIn: Request, action: Action[Request, Response]): Future[Response] = {
    if (fileResolver.hasFile(requestIn.uri) && requestIn.uri != '/') {
      (for {
        fh <- fileResolver.getInputStream(requestIn.uri)
        bytes <- Try(IOUtils.toByteArray(fh))
        result <- Try(fh.read(bytes))
      } yield {
        val mimeType = MimeTypes.fileExtensionOf('.' + requestIn.uri.toString.split('.').last)
        responseBuilder.withContentType(mimeType).withContent(ChannelBuffers.copiedBuffer(bytes)).toFuture
      }).get
    } else {
      action(requestIn)
    }
  }
}
