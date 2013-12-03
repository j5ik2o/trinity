package org.sisioh.trinity.domain.mvc.http

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.sisioh.trinity.domain.io.http.{Charset, Charsets}

/**
 * `JValue` のための[[org.sisioh.trinity.domain.mvc.http.ResponseRenderer]]。
 *
 * @param jValue `org.json4s.JValue`
 * @param charset [[org.sisioh.trinity.domain.io.http.Charset]]
 */
case class JSON4SRenderer(jValue: JValue, charset: Charset = Charsets.UTF_8) extends ResponseRenderer {

  def render(responseBuilder: ResponseBuilder): Unit =
    responseBuilder.
      withContent(compact(jValue), charset).
      withContentType(JSON4SRenderer.ContentTypeName + charset.toString().toLowerCase)

}

/**
 * コンパニオンオブジェクト。
 */
object JSON4SRenderer {

  private val ContentTypeName = "application/json; charset="

}
