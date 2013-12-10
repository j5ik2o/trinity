package org.sisioh.trinity.domain.io.util

import org.sisioh.trinity.domain.io.http.{Response, Request}

object RequestDumpUtil {

  /**
   * リクエストヘッダの内容を文字列バッファに編集します。
   *
   * @param sb      文字列バッファ
   * @param request リクエスト
   * @param lf      改行文字
   * @param indent  インデント
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
   * リクエスト属性の内容を文字列バッファに編集します。
   *
   * @param sb      文字列バッファ
   * @param request リクエスト
   * @param lf      改行文字
   * @param indent  インデント
   */
  def dumpRequestAttributes(sb: StringBuilder, request: Request, lf: String, indent: String) {
    val headers = request.attributes.map {
      e =>
        Seq(indent, "[request]", e._1, "=", e._2, lf).mkString(" ")
    }.mkString
    sb.append(headers)
  }


  /**
   * クッキーの内容を文字列バッファに編集します。
   *
   * @param sb      文字列バッファ
   * @param request リクエスト
   * @param lf      改行文字
   * @param indent  インデント
   */
  def dumpCookies(sb: StringBuilder, request: Request, lf: String, indent: String) {
    val cookies = request.cookies.map {
      e =>
        Seq(indent, "[cookie]", e.name, "=", e.value, lf).mkString(" ")
    }.mkString
    sb.append(cookies)
  }


  /**
   * リクエストパラメータの内容を文字列バッファに編集します。
   *
   * @param sb           文字列バッファ
   * @param request           リクエスト
   * @param lf            改行文字
   * @param indent            インデント
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
   * リクエストのプロパティを文字列バッファに編集します。
   *
   * @param sb
     * 文字列バッファ
   * @param request
     * リクエスト
   * @param lf
     * 改行文字
   * @param indent
     * インデント
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
   * レスポンスのプロパティを文字列バッファに編集します。
   *
   * @param sb 文字列バッファ
   * @param response レスポンス
   * @param lf 改行文字
   * @param indent インデント
   */
  def dumpResponseProperties(sb: StringBuilder, response: Response, lf: String, indent: String) {
    sb.append(indent)
    sb.append("Response class=" + response.getClass.getName).append(
      ", instance=").append(response.toString().trim())
    sb.append(lf);
  }


}
