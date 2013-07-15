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

import com.twitter.finagle.http.{Response => FinagleResponse, Request => FinagleRequest, Status}
import org.jboss.netty.buffer.{ChannelBuffers, ChannelBuffer}
import ChannelBuffers._
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.util.CharsetUtil._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import com.twitter.util.{Await, Future}
import scala.collection.JavaConverters._
import scala.language.implicitConversions

/**
 * Trinity内で扱うレスポンスを表す値オブジェクト。
 *
 * @param status
 * @param headers
 * @param cookies
 * @param body
 */
case class TrintiyResponse
(status: HttpResponseStatus = Status.Ok,
 headers: Map[String, AnyRef] = Map.empty,
 cookies: Seq[Cookie] = Seq.empty,
 body: Option[ChannelBuffer] = None) {

  def this(status: Int,
           headers: Map[String, AnyRef],
           cookies: Seq[Cookie],
           body: Option[ChannelBuffer]) =
    this(HttpResponseStatus.valueOf(status), headers, cookies, body)

  def bodyAsString: Option[String] = body.map(_.toString(UTF_8))

  def toFinagleResponse: FinagleResponse = {
    val result = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status)
    headers.foreach {
      case (k, v: Iterable[_]) =>
        result.setHeader(k, v.asJava)
      case (k, v) =>
        result.setHeader(k, v)
    }
    if (!cookies.isEmpty) {
      val cookieEncoder = new CookieEncoder(true)
      cookies.foreach {
        xs =>
          cookieEncoder.addCookie(xs)
      }
      result.setHeader("Set-Cookie", cookieEncoder.encode)
    }
    body.foreach {
      b =>
        result.setContent(b)
    }
    FinagleResponse(result)
  }

}

/**
 * [[org.sisioh.trinity.domain.http.TrinityResponseBuilder]]のためのビルダ。
 *
 * @param responseFuture `Future`にラップされた[[org.sisioh.trinity.domain.http.TrintiyResponse]]
 */
case class TrinityResponseBuilder
(private val responseFuture: Future[TrintiyResponse] = Future(TrintiyResponse())) {

  def withStatus
  (status: Int): TrinityResponseBuilder = {
    withStatus(HttpResponseStatus.valueOf(status))
  }

  def withStatus
  (status: HttpResponseStatus): TrinityResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(status = status)
    }
    TrinityResponseBuilder(newResposeFuture)
  }

  def withCookie
  (tuple: (String, String)): TrinityResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(cookies = response.cookies :+ new DefaultCookie(tuple._1, tuple._2))
    }
    TrinityResponseBuilder(newResposeFuture)
  }

  def withCookie
  (cookie: Cookie): TrinityResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(cookies = response.cookies :+ cookie)
    }
    TrinityResponseBuilder(newResposeFuture)
  }

  def withHeader
  (header: (String, String)): TrinityResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(headers = response.headers + header)
    }
    TrinityResponseBuilder(newResposeFuture)
  }

  def withBody
  (body: Array[Byte]): TrinityResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(body = Some(copiedBuffer(body)))
    }
    TrinityResponseBuilder(newResposeFuture)
  }

  def withBody
  (body: => String): TrinityResponseBuilder = {
    val newResposeFuture = responseFuture.map {
      response =>
        response.copy(body = Some(copiedBuffer(body, UTF_8)))
    }
    TrinityResponseBuilder(newResposeFuture)
  }

  def withBodyRenderer
  (bodyRenderer: BodyRenderer): TrinityResponseBuilder = {
    val newResposeFuture = bodyRenderer.render.flatMap {
      body =>
        responseFuture.map {
          response =>
            response.copy(body = Some(copiedBuffer(body, UTF_8)))
        }
    }
    TrinityResponseBuilder(newResposeFuture)
  }

  def withPlain
  (body: => String): TrinityResponseBuilder = {
    withHeader("Content-Type", "text/plain").withBody(body)
  }

  def withHtml(body: => String) = {
    withHeader("Content-Type", "text/html").withBody(body)
  }


  def withJson(jValue: => JValue): TrinityResponseBuilder = {
    withHeader("Content-Type", "application/json").withBody(compact(jValue))
  }

  def withNothing = {
    withHeader("Content-Type", "text/plain").withBody("")
  }

  def withOk = withStatus(HttpResponseStatus.OK)

  def withNotFound = withStatus(HttpResponseStatus.NOT_FOUND)

  /**
   * `Future`にラップされた[[org.sisioh.trinity.domain.http.TrintiyResponse]]を返す。
   *
   * @return `Future`にラップされた[[org.sisioh.trinity.domain.http.TrintiyResponse]]
   */
  def toTrinityResponseFuture: Future[TrintiyResponse] = responseFuture

  /**
   * [[org.sisioh.trinity.domain.http.TrintiyResponse]]を取得する。
   *
   * @return [[org.sisioh.trinity.domain.http.TrintiyResponse]]
   */
  def getTrinityResponse: TrintiyResponse = Await.result(responseFuture)

  /**
   * `Future`にラップされた `com.twitter.finagle.http.Response` を返す。
   *
   * @return `Future`にラップされた `com.twitter.finagle.http.Response`
   */
  def toFinagleResponseFuture: Future[FinagleResponse] = responseFuture.map(_.toFinagleResponse)

  /**
   * `com.twitter.finagle.http.Response` を取得する。
   *
   * @return `com.twitter.finagle.http.Response`
   */
  def getFinagleResponse: FinagleResponse = Await.result(toFinagleResponseFuture)


}

trait TrinityResponseImplicitSupport {

  implicit def convertToFingaleResponse(res: TrintiyResponse) =
    res.toFinagleResponse

  implicit def convertToFutureFinagleResponse(res: Future[TrintiyResponse]) =
    res.map(_.toFinagleResponse)

}
