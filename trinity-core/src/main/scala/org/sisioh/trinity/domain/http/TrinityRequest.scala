/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
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
package org.sisioh.trinity.domain.http

import com.google.common.base.Splitter
import com.twitter.finagle.http.{Request => FinagleRequest, RequestProxy}
import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.fileupload.MultiPartItem
import scala.collection.JavaConversions._
import util.Sorting

/**
 * Trinity内で扱うリクエストを表す値オブジェクト。
 *
 * @param rawRequest
 * @param routeParams
 * @param error
 */
case class TrinityRequest
(rawRequest: FinagleRequest,
 routeParams: Map[String, String] = Map.empty,
 error: Option[Throwable] = None)
  extends RequestProxy with LoggingEx {

  val request = rawRequest

  val multiParams: Map[String, MultiPartItem] =
    if (method == HttpMethod.POST) {
      getContent().markReaderIndex()
      val m = MultiPartItem.fromRequest(request)
      getContent().resetReaderIndex()
      m
    } else Map.empty[String, MultiPartItem]

  def accepts: Seq[ContentType] = {
    val acceptOpt = Option(getHeader("Accept"))
    acceptOpt.map {
      accept =>
        val acceptParts = Splitter.on(',').split(accept).toArray
        Sorting.quickSort(acceptParts)(AcceptOrdering)
        acceptParts.map {
          xs =>
            val part = Splitter.on(";q=").split(xs).toArray.head
            ContentType.valueOf(part).getOrElse(ContentType.All)
        }.toSeq
    }.getOrElse(Seq.empty[ContentType])
  }

}

object AcceptOrdering extends Ordering[String] {

  def getWeight(str: String): Double = {
    val parts = Splitter.on(';').split(str).toArray
    if (parts.length < 2) {
      1.0
    } else {
      try {
        Splitter.on("q=").split(parts(1)).toArray.last.toFloat
      } catch {
        case e: java.lang.NumberFormatException =>
          1.0
      }
    }
  }

  def compare(a: String, b: String) =
    getWeight(b) compare getWeight(a)

}
