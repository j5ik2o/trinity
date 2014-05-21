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
package org.sisioh.trinity.domain.mvc.filter

import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.io.util.RequestDumpUtil
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

/**
<<<<<<< HEAD
 * [[org.sisioh.trinity.domain.io.http.Request]]をログにダンプするためのフィルター。
=======
 * Represents the filter for [[Request]] dumpping.
>>>>>>> release/v1.0.7
 *
 * @param executor [[scala.concurrent.ExecutionContext]]
 */
case class RequestDumpFilter(implicit executor: ExecutionContext)
  extends SimpleFilter[Request, Response] with LoggingEx {

  private val INDENT = "  "

  private val LF = System.getProperty("line.separator")

  private def dumpBefore(request: Request): Future[Request] = {
    val sb = new StringBuilder
    sb.append(LF)
    sb.append(LF)
    sb.append("** before *****************************************: ")
    sb.append(request.path)
    sb.append(LF)
    RequestDumpUtil.dumpRequestProperties(sb, request, LF, INDENT)
    RequestDumpUtil.dumpRequestParameters(sb, request, LF, INDENT)
    RequestDumpUtil.dumpRequestAttributes(sb, request, LF, INDENT)
    RequestDumpUtil.dumpCookies(sb, request, LF, INDENT)
    RequestDumpUtil.dumpRequestHeaders(sb, request, LF, INDENT)
    debug(sb.toString())
    Future.successful(request)
  }

  private def dumpAfter(request: Request, response: Response): Future[Request] = {
    val sb = new StringBuilder
    sb.append(LF)
    sb.append(LF)
    sb.append("** after *****************************************: ")
    sb.append(request.path)
    sb.append(LF)
    RequestDumpUtil.dumpResponseProperties(sb, response, LF, INDENT)
    RequestDumpUtil.dumpRequestParameters(sb, request, LF, INDENT)
    RequestDumpUtil.dumpRequestAttributes(sb, request, LF, INDENT)
    RequestDumpUtil.dumpCookies(sb, request, LF, INDENT)
    RequestDumpUtil.dumpRequestHeaders(sb, request, LF, INDENT)
    debug(sb.toString())
    Future.successful(request)
  }

  def apply(requestIn: Request, action: Action[Request, Response]): Future[Response] = {
    dumpBefore(requestIn).flatMap {
      action(_)
    }.andThen {
      case Success(response) =>
        dumpAfter(requestIn, response)
      case _ =>
    }
  }
}
