/*
 * Copyright 2013 Sisioh Project and others. (http://sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
case class JSON4SRenderer(jValue: JValue, charset: Option[Charset] = None) extends ResponseRenderer {

  def render(responseBuilder: ResponseBuilder): Unit =
    responseBuilder.
      withContent(compact(jValue), charset.getOrElse(Charsets.UTF_8)).
      withContentType(JSON4SRenderer.ContentTypeName + charset.map("; charset=" + _.toObject.name().toLowerCase).getOrElse(""))

}

/**
 * コンパニオンオブジェクト。
 */
object JSON4SRenderer {

  private val ContentTypeName = "application/json"

}
