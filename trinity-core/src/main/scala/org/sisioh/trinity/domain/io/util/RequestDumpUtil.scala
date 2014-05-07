package org.sisioh.trinity.domain.io.util

import org.sisioh.trinity.domain.io.http.{Response, Request}

/**
 * Represents the utility class for dumping the request.
 */
object RequestDumpUtil {

  /**
   * Dumps the contents the headers has to the string buffer.
   *
   * @param sb      string buffer
   * @param request [[Request]]
   * @param lf      LF
   * @param indent  indent
   */
  def dumpRequestHeaders(sb: StringBuilder,
                         request: Request, lf: String,
                         indent: String) {
    val headers = request.headers.map {
      e =>
        Seq(indent, "[header]", e._1.asString, "=", e._2, lf).mkString(" ")
    }.mkString
    sb.append(headers)
  }

  /**
   * Dumps the contents the attributes has to the string buffer.
   *
   * @param sb      string buffer
   * @param request [[Request]]
   * @param lf      LF
   * @param indent  indent
   */
  def dumpRequestAttributes(sb: StringBuilder, request: Request, lf: String, indent: String) {
    val headers = request.attributes.map {
      e =>
        Seq(indent, "[request]", e._1, "=", e._2, lf).mkString(" ")
    }.mkString
    sb.append(headers)
  }


  /**
   * Dumps the contents the cookies has to the string builder.
   *
   * @param sb      string buffer
   * @param request [[Request]]
   * @param lf      LF
   * @param indent  indent
   */
  def dumpCookies(sb: StringBuilder, request: Request, lf: String, indent: String) {
    val cookies = request.cookies.map {
      e =>
        Seq(indent, "[cookie]", e.name, "=", e.value, lf).mkString(" ")
    }.mkString
    sb.append(cookies)
  }


  /**
   * Dumps the contents the request parameters has to string buffer.
   *
   * @param sb      string buffer
   * @param request [[Request]]
   * @param lf      LF
   * @param indent  indent
   */
  def dumpRequestParameters(sb: StringBuilder, request: Request, lf: String, indent: String) {
    val params = request.getParamNames.map {
      name =>
        val values = request.getParamAsSeq(name).mkString(", ")
        Seq(indent, "[param]", name, "=", values, lf).mkString(" ")
    }.mkString
    sb.append(params)
  }

  /**
   * Dumps the request properties to string buffer.
   *
   * @param sb      string buffer
   * @param request [[Request]]
   * @param lf      LF
   * @param indent  indent
   */
  def dumpRequestProperties(sb: StringBuilder, request: Request, lf: String, indent: String) {
    sb.append(indent)
    sb.append("Request class=" + request.getClass.getName).append(", instance=").append(request.toString().trim())

    sb.append(lf)
    sb.append(indent)

    //    sb.append("RequestedSessionId=")
    //      .append(request.getRequestedSessionId());

    sb.append(lf)
    sb.append(indent)

    sb.append("REQUEST_URI=").append(request.uri)

    sb.append(lf)
    sb.append(indent)
    sb.append("CharacterEncoding=" + request.charset.getOrElse(""))
    sb.append(", ContentLength=").append(request.contentLength.getOrElse(""))
    sb.append(", ContentType=").append(request.contentType.getOrElse(""))
    sb.append(", Locale=").append(request.location.getOrElse(""))
    sb.append(lf).append(indent)

    sb.append("SERVER_PROTOCOL=").append(request.protocolVersion)
    sb.append(", REMOTE_ADDR=").append(request.remoteAddress)
    sb.append(", REMOTE_HOST=").append(request.remoteHost)
    //    sb.append(", SERVER_NAME=").append(request.getServerName());
    //    sb.append(", SERVER_PORT=").append(request.getServerPort());

    sb.append(lf).append(indent)

    sb.append("ContextPath=").append(request.path)
    sb.append(", REQUEST_METHOD=").append(request.method)
    sb.append(", QUERY_STRING=").append(request.queryString)
    //    sb.append(", PathInfo=").append(request.getPathInfo());
    //    sb.append(", RemoteUser=").append(request.getRemoteUser());
    //
    sb.append(lf)
  }


  /**
   * Dumps the response properties to string buffer.
   *
   * @param sb      string buffer
   * @param response [[Response]]
   * @param lf      LF
   * @param indent  indent
   */
  def dumpResponseProperties(sb: StringBuilder, response: Response, lf: String, indent: String) {
    sb.append(indent)
    sb.append("Response class=" + response.getClass.getName).append(
      ", instance=").append(response.toString().trim())
    sb.append(lf)
  }


}
