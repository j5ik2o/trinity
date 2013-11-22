package org.sisioh.trinity.domain.mvc.http

import java.nio.charset.Charset
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.sisioh.trinity.domain.io.http.CharsetUtil

case class JSON4SRender(jValue: JValue, charset: Charset = CharsetUtil.UTF_8) extends ResponseRender {

  def render(responseBuilder: ResponseBuilder): Unit =
    responseBuilder.
      withContent(compact(jValue), charset).
      withContentType("application/json; charset=" + charset.toString.toLowerCase)

}
