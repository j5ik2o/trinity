package org.sisioh.trinity.domain.mvc.resource

import java.io.{IOException, File}
import org.apache.commons.io.IOUtils
import org.sisioh.trinity.domain.io.buffer.ChannelBuffers
import org.sisioh.trinity.domain.io.http.{MimeTypes, HeaderNames, ResponseStatus}
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Request, Response}
import org.sisioh.trinity.domain.mvc.{Environment, SimpleFilter}
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class FileResourceReadFilter(environment: Environment.Value, localPath: File) extends SimpleFilter[Request, Response] {

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
        _ <- if (result > 0) Success(bytes) else Failure(new IOException())
      } yield {
        val response = requestIn.response
        val mimeType = MimeTypes.fileExtensionOf('.' + requestIn.uri.toString.split('.').last)
        val newResponse = response.withHeader(HeaderNames.ContentType, mimeType).withResponseStatus(ResponseStatus.Ok).
          withContent(ChannelBuffers.copiedBuffer(bytes))
        Future.successful(Response(newResponse))
      }).get
    } else {
      action(requestIn)
    }
  }
}
