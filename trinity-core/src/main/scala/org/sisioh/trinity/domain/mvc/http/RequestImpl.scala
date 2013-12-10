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

import org.jboss.netty.handler.codec.http.multipart.{MixedFileUpload, HttpPostRequestDecoder}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer.toNetty
import org.sisioh.trinity.domain.io.http
import org.sisioh.trinity.domain.io.http.{Request => IORequest, _}
import org.sisioh.trinity.domain.mvc.GlobalSettings
import org.sisioh.trinity.domain.mvc.action.Action
import scala.collection.JavaConverters._
import scala.util.Try
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import scala.Some

private[http]
class RequestImpl
(override val underlying: IORequest,
 val action: Option[Action[Request, Response]],
 val routeParams: Map[String, String],
 val globalSettings: Option[GlobalSettings[Request, Response]],
 val error: Option[Throwable])
  extends AbstractRequestProxy(underlying) with Request {

  def this(method: Methods.Value = Methods.Get,
           uri: String = "/",
           headers: Seq[(HeaderName, Any)] = Seq.empty,
           cookies: Seq[Cookie] = Seq.empty,
           attributes: Map[String, Any] = Map.empty,
           content: ChannelBuffer = ChannelBuffer.empty,
           action: Option[Action[Request, Response]] = None,
           routeParams: Map[String, String] = Map.empty,
           globalSettings: Option[GlobalSettings[Request, Response]] = None,
           error: Option[Throwable] = None,
           isMutable: Boolean = false,
           protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11) =
    this(
      IORequest(
        method, uri, headers,
        cookies, attributes, content, isMutable, protocolVersion
      ),
      action, routeParams, globalSettings, error
    )

  val toUnderlyingAsFinagle = underlying.toUnderlyingAsFinagle

  protected def createInstance(message: this.type, attributes: Map[String, Any]): this.type =
    new RequestImpl(
      message.underlying.withAttributes(attributes),
      message.action,
      message.routeParams,
      message.globalSettings,
      message.error
    ).asInstanceOf[this.type]


  def multiParams: Try[Map[String, MultiPartItem]] = Try {
    if (method == Methods.Post) {
      this.synchronized {
        content.markReaderIndex()
        val httpPostRequestDecoder = new HttpPostRequestDecoder(toUnderlyingAsFinagle)
        val m = if (httpPostRequestDecoder.isMultipart) {
          httpPostRequestDecoder.getBodyHttpDatas.asScala.map {
            data =>
              data.getName -> MultiPartItem(data.asInstanceOf[MixedFileUpload])
          }.toMap
        } else Map.empty[String, MultiPartItem]
        content.resetReaderIndex()
        m
      }
    } else Map.empty[String, MultiPartItem]
  }

  def response: http.Response =
    Response(underlying.response)

  def withAction(action: Option[Action[Request, Response]]): this.type =
    new RequestImpl(underlying, action, routeParams, globalSettings, error).asInstanceOf[this.type]

  def withRouteParams(routeParams: Map[String, String]): this.type =
    new RequestImpl(underlying, action, routeParams, globalSettings, error).asInstanceOf[this.type]

  def withError(error: Throwable): this.type =
    new RequestImpl(underlying, action, routeParams, globalSettings, Some(error)).asInstanceOf[this.type]

  val attributes: Map[String, Any] = underlying.attributes

  def withAttributes(attributes: Map[String, Any]): this.type =
    new RequestImpl(underlying.withAttributes(attributes), action, routeParams, globalSettings, error).asInstanceOf[this.type]

  def withAttributes(attributes: (String, Any)*): this.type =
    new RequestImpl(underlying.withAttributes(attributes: _*), action, routeParams, globalSettings, error).asInstanceOf[this.type]

  def withoutAllAttributes(): this.type =
    new RequestImpl(underlying.withoutAllAttributes(), action, routeParams, globalSettings, error).asInstanceOf[this.type]


}
