package org.sisioh.trinity.domain.mvc.action

import java.io.{IOException, InputStream, PrintWriter, StringWriter}
import org.apache.commons.io.IOUtils
import org.sisioh.trinity.domain.io.http.{HeaderNames, ResponseStatus, ProtocolVersion}
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffers

/**
 * INTERNAL_SERVER_ERROR(500)を返す[[org.sisioh.trinity.domain.mvc.action.Action]]。
 */
case object InternalServerErrorAction extends Action[Request, Response] {
  private def getResourceInputStream(path: String): Try[InputStream] = {
    Try(getClass.getResourceAsStream(path)).flatMap {
      stream =>
        Option(stream).map {
          s =>
            Success(s)
        }.getOrElse(Failure(new Exception))
    }
  }

  def apply(request: Request): Future[Response] = {
    val exception = request.error.get
    val sw = new StringWriter()
    val pw = new PrintWriter(sw)
    exception.printStackTrace(pw)
    (for {
      fh <- getResourceInputStream("/500.html")
      bytes <- Try(IOUtils.toByteArray(fh))
      result <- Try(fh.read(bytes))
    } yield {
      val html = new String(bytes).replace("$STACK_TRACE", sw.toString)
      Future.successful(
        Response(
          ResponseStatus.InternalServerError,
          ProtocolVersion.Http11
        ).withHeader(HeaderNames.ContentType, "text/html").withContentAsString(html)
      )
    }).get
  }

}
