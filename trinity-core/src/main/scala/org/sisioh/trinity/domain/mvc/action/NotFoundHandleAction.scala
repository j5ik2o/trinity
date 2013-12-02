package org.sisioh.trinity.domain.mvc.action

import java.lang.String
import org.apache.commons.io.IOUtils
import org.sisioh.trinity.domain.io.http.{HeaderNames, ResponseStatus, ProtocolVersion}
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.util.ResourceUtil
import scala.concurrent.Future
import scala.util.Try

/**
 * NOT_FOUND(404)を返す[[org.sisioh.trinity.domain.mvc.action.Action]]。
 */
case object NotFoundHandleAction extends Action[Request, Response] {

  def apply(request: Request): Future[Response] = {
    (for {
      fh <- ResourceUtil.getResourceInputStream("/404.html")
      bytes <- Try(IOUtils.toByteArray(fh))
      result <- Try(fh.read(bytes))
    } yield {
      val html = new String(bytes)
      Future.successful(
        Response(
          ResponseStatus.NotFound,
          ProtocolVersion.Http11
        ).withHeader(HeaderNames.ContentType, "text/html").withContentAsString(html)
      )
    }).get
  }

}
