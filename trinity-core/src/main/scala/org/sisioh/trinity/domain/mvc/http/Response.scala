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

import com.twitter.finagle.http.{Response => FinagleResponse}
import org.sisioh.trinity.domain.io.http.{Response => IOResponse, _}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

/**
 * MVCのためのレスポンス。
 */
trait Response extends Message with ResponseProxy {

  def encodeString(): String = toUnderlyingAsFinagle.encodeString()

}

/**
 * コンパニオンオブジェクト。
 */
object Response {

  /**
   * ファクトリメソッド。
   * 　
   * @param underlying
   * @return
   */
  private[trinity] def apply(underlying: IOResponse): Response = new ResponseImpl(underlying)

  /**
   * ファクトリメソッド。
   *
   * @param responseStatus
   * @param protocolVersion
   * @return
   */
  def apply(responseStatus: ResponseStatus.Value = ResponseStatus.Ok,
            headers: Seq[(HeaderName, Any)] = Seq.empty,
            cookies: Seq[Cookie] = Seq.empty,
            attributes: Map[String, Any] = Map.empty[String, Any],
            content: ChannelBuffer = ChannelBuffer.empty,
            isMutable: Boolean = false,
            protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11): Response =
    new ResponseImpl(responseStatus, headers, cookies, attributes, content, isMutable, protocolVersion)

}
