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

import com.google.common.base.Splitter
import com.twitter.finagle.http.{Request => FinagleRequest}
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.io.http.{Request => IORequest, _}
import org.sisioh.trinity.domain.mvc.GlobalSettings
import org.sisioh.trinity.domain.mvc.action.Action
import scala.collection.JavaConversions.iterableAsScalaIterable
import scala.concurrent.Future
import scala.util.{Try, Sorting}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

/**
 * Represents [[org.sisioh.trinity.domain.io.http.Request]] that was enhanced for MVC.
 */
trait Request extends Message with RequestProxy with LoggingEx {

  override def equals(obj: Any): Boolean = obj match {
    case that: Request =>
      super.equals(that) &&
        action == that.action &&
        routeParams == that.routeParams &&
        globalSettings == that.globalSettings &&
        error == that.error
    case _ => false
  }

  override def hashCode: Int =
    31 * (super.hashCode + toUnderlyingAsFinagle.## +
      action.## + routeParams.## + globalSettings.## + error.##)

  override def toString =
    Seq(
      s"protocolVersion = $protocolVersion",
      s"method = $method",
      s"uri = $uri",
      s"headers = $headers",
      s"content = $content",
      s"action = $action",
      s"routeParams = $routeParams",
      s"globalSetting = $globalSettings",
      s"error = $error"
    ).mkString("Request(", ", ", ")")

  val action: Option[Action[Request, Response]]

  def withAction(action: Option[Action[Request, Response]]): this.type

  def encodeBytes: Array[Byte] = toUnderlyingAsFinagle.encodeBytes()

  def encodeString: String = toUnderlyingAsFinagle.encodeString()

  def routeParams: Map[String, String]

  def withRouteParams(routeParams: Map[String, String]): this.type

  def multiParams: Try[Map[String, MultiPartItem]]

  def accepts: Seq[ContentType] = {
    val accept = getHeader(HeaderNames.Accept)
    accept.fold(Seq.empty[ContentType]) {
      accept =>
        val acceptParts = Splitter.on(',').split(accept).toArray
        Sorting.quickSort(acceptParts)(AcceptOrdering)
        acceptParts.map {
          xs =>
            val part = Splitter.on(";q=").split(xs).toArray.head
            ContentTypes.valueOf(part).getOrElse(ContentTypes.All)
        }.toSeq
    }
  }

  val error: Option[Throwable]

  def withError(error: Throwable): this.type

  /**
   * この `Request` に割り当てられた `Action` を実行する。
   *
   * @param defaultAction [[org.sisioh.trinity.domain.mvc.action.Action]]
   * @return `Future`でラップされた [[org.sisioh.trinity.domain.mvc.http.Response]]
   */
  def execute(defaultAction: Action[Request, Response]): Future[Response] = withDebugScope(s"$toString : execute") {
    action.fold(defaultAction(this))(_(this))
  }

  val globalSettings: Option[GlobalSettings[Request, Response]]

}

/**
 * コンパニオンオブジェクト。
 */
object Request {

  private[trinity] def fromUnderlying(underlying: IORequest,
                                      actionOpt: Option[Action[Request, Response]] = None,
                                      routeParams: Map[String, String] = Map.empty,
                                      globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None,
                                      errorOpt: Option[Throwable] = None): Request =
    new RequestImpl(
      underlying,
      actionOpt,
      routeParams,
      globalSettingsOpt,
      errorOpt
    )

  def apply(method: Methods.Value = Methods.Get,
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
    new RequestImpl(
      method,
      uri,
      headers,
      cookies,
      attributes,
      content,
      action,
      routeParams,
      globalSettings,
      error,
      isMutable,
      protocolVersion
    )

}
