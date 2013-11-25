package org.sisioh.trinity.domain.mvc

import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.io.util.RequestDumpUtil
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class RequestDumpFilter(implicit executor: ExecutionContext) extends SimpleFilter[Request, Response] with LoggingEx {

  private val INDENT = "  "

  private val LF = System.getProperty("line.separator")

  def dumpBefore(request: Request): Future[Request] = {
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

  def dumpAfter(request: Request, response: Response): Future[Request] = {
    val sb = new StringBuilder
    sb.append(LF)
    sb.append(LF)
    sb.append("** after *****************************************: ")
    sb.append(request.path)
    sb.append(LF)
    RequestDumpUtil.dumpResponseProperties(sb, response, LF, INDENT);
    RequestDumpUtil.dumpRequestParameters(sb, request, LF, INDENT);
    RequestDumpUtil.dumpRequestAttributes(sb, request, LF, INDENT);
    RequestDumpUtil.dumpCookies(sb, request, LF, INDENT);
    RequestDumpUtil.dumpRequestHeaders(sb, request, LF, INDENT);

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
