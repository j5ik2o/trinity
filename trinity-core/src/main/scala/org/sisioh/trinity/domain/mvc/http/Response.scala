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

import com.twitter.finagle.http.{ Response => FinagleResponse }
import org.sisioh.trinity.domain.io.http
import org.sisioh.trinity.domain.io.http.{ Response => IOResponse, ResponseStatus, ProtocolVersion, ResponseProxy }

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
  def apply(underlying: http.Response): Response = new ResponseImpl(underlying)

  /**
   * ファクトリメソッド。
   *
   * @param responseStatus
   * @param protocolVersion
   * @return
   */
  def apply(responseStatus: ResponseStatus.Value = ResponseStatus.Ok,
            protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11): Response =
    new ResponseImpl(responseStatus, protocolVersion)

}
