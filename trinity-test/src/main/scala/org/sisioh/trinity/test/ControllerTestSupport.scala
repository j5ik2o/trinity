package org.sisioh.trinity.test

import com.twitter.finagle.http.{Request => FinagleRequest, Method}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.jboss.netty.handler.codec.http.multipart.{HttpPostRequestEncoder, DefaultHttpDataFactory}
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.controller.{GlobalSettings, Controller}

trait ControllerTestSupport extends LoggingEx {

  /**
   * HTTPリクエストを生成する。
   *
   * @param method HTTPメソッド
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @return [[com.twitter.finagle.http.Request]]
   */
  protected def newRequest
  (method: HttpMethod,
   path: String,
   content: Option[Content],
   headers: Map[String, String]): FinagleRequest =
    withDebugScope(s"newRequest($method, $path, $content, $headers)") {
      val request = content match {
        case Some(StringContent(v)) =>
          val result = FinagleRequest(path)
          result.httpRequest.setMethod(method)
          result.contentString = v
          result
        case Some(MapContent(v)) if method == Method.Post =>
          val result = FinagleRequest(path)
          result.httpRequest.setMethod(method)
          val dataFactory = new DefaultHttpDataFactory(false) // we don't use disk
        val encoder = new HttpPostRequestEncoder(dataFactory, result, false)
          v.toList.foreach {
            case (k, v) =>
              encoder.addBodyAttribute(k, v)
          }
          FinagleRequest(encoder.finalizeRequest())
        case Some(MapContent(v)) =>
          val result = FinagleRequest(path, v.toList: _*)
          result.httpRequest.setMethod(method)
          result
        case _ =>
          val result = FinagleRequest(path)
          result.httpRequest.setMethod(method)
          result
      }
      headers.foreach {
        header =>
          request.httpRequest.setHeader(header._1, header._2)
      }
      scopedDebug(s"request = $request")
      scopedDebug(s"request.contentString = ${request.contentString}")
      request
    }

  /**
   * テストのためのリクエストを生成する。
   *
   * @param method HTTPメソッド
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param application [[org.sisioh.trinity.application.TrinityApplication]]
   * @param controller [[org.sisioh.trinity.domain.controller.Controller]]
   * @return [[org.sisioh.trinity.test.MockResponse]]
   */
  protected def buildRequest
  (method: HttpMethod,
   path: String,
   content: Option[Content],
   headers: Map[String, String])
  (implicit application: TrinityApplication, controller: Controller): MockResponse

  /**
   * GETメソッドをテストする。
   *
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param f レスポンスを検証する関数
   * @param application [[org.sisioh.trinity.application.TrinityApplication]]
   * @param controller [[org.sisioh.trinity.domain.controller.Controller]]
   * @tparam T 関数の戻り値の型
   * @return 関数の戻り値
   */
  protected def testGet[T](path: String, content: Option[Content] = None, headers: Map[String, String] = Map())
                          (f: MockResponse => T)(implicit application: TrinityApplication, controller: Controller): T = {
    f(buildRequest(Method.Get, path, content, headers))
  }

  /**
   * POSTメソッドをテストする。
   *
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param f レスポンスを検証する関数
   * @param application [[org.sisioh.trinity.application.TrinityApplication]]
   * @param controller [[org.sisioh.trinity.domain.controller.Controller]]
   * @tparam T 関数の戻り値の型
   * @return 関数の戻り値
   */
  protected def testPost[T](path: String, content: Option[Content] = None, headers: Map[String, String] = Map())
                           (f: MockResponse => T)(implicit application: TrinityApplication, controller: Controller): T = {
    f(buildRequest(Method.Post, path, content, headers))
  }

  /**
   * PUTメソッドをテストする。
   *
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param f レスポンスを検証する関数
   * @param application [[org.sisioh.trinity.application.TrinityApplication]]
   * @param controller [[org.sisioh.trinity.domain.controller.Controller]]
   * @tparam T 関数の戻り値の型
   * @return 関数の戻り値
   */
  protected def testPut[T](path: String, content: Option[Content] = None, headers: Map[String, String] = Map())
                          (f: MockResponse => T)(implicit application: TrinityApplication, controller: Controller): T = {
    f(buildRequest(Method.Put, path, content, headers))
  }

  /**
   * DELETEメソッドをテストする。
   *
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param f レスポンスを検証する関数
   * @param application [[org.sisioh.trinity.application.TrinityApplication]]
   * @param controller [[org.sisioh.trinity.domain.controller.Controller]]
   * @tparam T 関数の戻り値の型
   * @return 関数の戻り値
   */
  protected def testDelete[T](path: String, content: Option[Content] = None, headers: Map[String, String] = Map())
                             (f: MockResponse => T)(implicit application: TrinityApplication, controller: Controller): T = {
    f(buildRequest(Method.Delete, path, content, headers))
  }

  /**
   * HEADメソッドをテストする。
   *
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param f レスポンスを検証する関数
   * @param application [[org.sisioh.trinity.application.TrinityApplication]]
   * @param controller [[org.sisioh.trinity.domain.controller.Controller]]
   * @tparam T 関数の戻り値の型
   * @return 関数の戻り値
   */
  protected def testHead[T](path: String, content: Option[Content] = None, headers: Map[String, String] = Map())
                           (f: MockResponse => T)(implicit application: TrinityApplication, controller: Controller): T = {
    f(buildRequest(Method.Head, path, content, headers))
  }

  protected def testPatch[T](path: String, content: Option[Content] = None, headers: Map[String, String] = Map())
                            (f: MockResponse => T)(implicit application: TrinityApplication, controller: Controller): T = {
    f(buildRequest(Method.Patch, path, content, headers))
  }

  /**
   * [[org.sisioh.trinity.domain.controller.GlobalSettings]]を取得する。
   *
   * @return [[org.sisioh.trinity.domain.controller.GlobalSettings]]
   */
  protected def getGlobalSettings: Option[GlobalSettings] = None
}
