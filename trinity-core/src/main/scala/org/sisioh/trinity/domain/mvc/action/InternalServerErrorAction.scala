package org.sisioh.trinity.domain.mvc.action

import java.io.{PrintWriter, StringWriter}
import org.apache.commons.io.IOUtils
import org.sisioh.trinity.domain.io.http.{HeaderNames, ResponseStatus, ProtocolVersion}
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.util.ResourceUtil
import scala.concurrent.Future
import scala.util.Try

/**
 * INTERNAL_SERVER_ERROR(500)を返す[[org.sisioh.trinity.domain.mvc.action.Action]]。
 */
case object InternalServerErrorAction extends Action[Request, Response] {

  def apply(request: Request): Future[Response] = {
    val exception = request.error.get
    val sw = new StringWriter()
    val pw = new PrintWriter(sw)
    exception.printStackTrace(pw)
    (for {
      fh <- ResourceUtil.getResourceInputStream("/500.html")
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
