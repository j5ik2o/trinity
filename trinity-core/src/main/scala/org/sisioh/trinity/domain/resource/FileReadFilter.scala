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
package org.sisioh.trinity.domain.resource

import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.http.ContentType
import org.apache.commons.io.IOUtils

object FileReadFilter {

  def apply(config: Config) =
    new FileReadFilter(config)

}

class FileReadFilter(config: Config)
  extends SimpleFilter[FinagleRequest, FinagleResponse]
  with LoggingEx {

  private val fileResolver = FileResolver(config)

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
