package org.sisioh.trinity.test

import com.twitter.finagle.http.{Method, RequestBuilder, Request => FinagleRequest}
import java.util.concurrent.TimeUnit
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http.HttpMethod
import org.jboss.netty.util.CharsetUtil
import org.sisioh.trinity.domain.io.http.HeaderName
import org.sisioh.trinity.domain.mvc.GlobalSettings
import org.sisioh.trinity.domain.mvc.http.{Request, Response}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.util.Try

trait ControllerTestSupport {

  case class TestServer(host: Option[String] = None, port: Option[Int] = None)

  trait TestContext {
    val server: TestServer
    val executor: ExecutionContext
  }

  protected val globalSettings: Option[GlobalSettings[Request, Response]] = None

  protected val requestTimeout = Duration(10, TimeUnit.SECONDS)

  protected val defaultHost = "localhost"

  protected val defaultPort = 7070

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
  (method: HttpMethod, path: String, content: Option[Content], headers: Map[HeaderName, String])
  (implicit testContext: TestContext): FinagleRequest = {
    val host = testContext.server.host.getOrElse(defaultHost)
    val port = testContext.server.port.getOrElse(defaultPort)
    def url = "http://" + host + ":" + port
    val request = content match {
      case Some(StringContent(v)) if method == Method.Post =>
        val httpRequest = RequestBuilder().url(url + path).
          build(method, Some(ChannelBuffers.copiedBuffer(v, CharsetUtil.UTF_8)))
        FinagleRequest(httpRequest)
      case Some(MapContent(v)) if method == Method.Post =>
        val httpRequest = RequestBuilder().url(url + path).
          addFormElement(v.toSeq: _*).buildFormPost(false)
        FinagleRequest(httpRequest)
      case Some(MapContent(v)) if method == Method.Get =>
        val params = v map {
          case (key, value) =>
            key + '=' + value
        } mkString("?", "&", "")
        val httpRequest = RequestBuilder.safeBuildGet(
          RequestBuilder.create().url(url + path + params)
        )
        FinagleRequest(httpRequest)
      case None if method == Method.Get =>
        val httpRequest = RequestBuilder().url(url + path).buildGet()
        FinagleRequest(httpRequest)
      case None if method == Method.Post =>
        val httpRequest = RequestBuilder().url(url + path).build(method, None)
        FinagleRequest(httpRequest)
      case Some(_) if method == Method.Get =>
        throw new IllegalArgumentException("Illegal request argument")
      case _ =>
        throw new IllegalArgumentException("Illegal request argument")
    }
    headers.foreach {
      header =>
        request.httpRequest.headers.set(header._1.asString, header._2)
    }
    request
  }

  /**
   * テストのためのリクエストを生成する。
   *
   * @param method HTTPメソッド
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @return [[org.sisioh.trinity.domain.mvc.http.Response]]
   */
  protected def buildRequest
  (method: HttpMethod,
   path: String,
   content: Option[Content],
   headers: Map[HeaderName, String],
   timeout: Duration = requestTimeout)
  (implicit textContext: TestContext): Try[Response]

  /**
   * GETメソッドをテストする。
   *
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param f レスポンスを検証する関数
   * @tparam T 関数の戻り値の型
   * @return 関数の戻り値
   */
  protected def testGet[T](path: String, content: Option[Content] = None, headers: Map[HeaderName, String] = Map(),
                           timeout: Duration = requestTimeout)
                          (f: Try[Response] => T)(implicit testContext: TestContext): T = {
    f(buildRequest(Method.Get, path, content, headers, timeout))
  }

  /**
   * POSTメソッドをテストする。
   *
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param f レスポンスを検証する関数
   * @tparam T 関数の戻り値の型
   * @return 関数の戻り値
   */
  protected def testPost[T](path: String, content: Option[Content] = None, headers: Map[HeaderName, String] = Map(),
                            timeout: Duration = requestTimeout)
                           (f: Try[Response] => T)(implicit testContext: TestContext): T = {
    f(buildRequest(Method.Post, path, content, headers, timeout))
  }

  /**
   * PUTメソッドをテストする。
   *
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param f レスポンスを検証する関数
   * @tparam T 関数の戻り値の型
   * @return 関数の戻り値
   */
  protected def testPut[T](path: String, content: Option[Content] = None, headers: Map[HeaderName, String] = Map(),
                           timeout: Duration = requestTimeout)
                          (f: Try[Response] => T)(implicit testContext: TestContext): T = {
    f(buildRequest(Method.Put, path, content, headers, timeout))
  }

  /**
   * DELETEメソッドをテストする。
   *
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param f レスポンスを検証する関数
   * @tparam T 関数の戻り値の型
   * @return 関数の戻り値
   */
  protected def testDelete[T](path: String, content: Option[Content] = None, headers: Map[HeaderName, String] = Map(),
                              timeout: Duration = requestTimeout)
                             (f: Try[Response] => T)(implicit testContext: TestContext): T = {
    f(buildRequest(Method.Delete, path, content, headers, timeout))
  }

  /**
   * HEADメソッドをテストする。
   *
   * @param path パス
   * @param content `Option`でラップした[[org.sisioh.trinity.test.Content]]
   * @param headers HTTPヘッダ
   * @param f レスポンスを検証する関数
   * @tparam T 関数の戻り値の型
   * @return 関数の戻り値
   */
  protected def testHead[T](path: String, content: Option[Content] = None, headers: Map[HeaderName, String] = Map(),
                            timeout: Duration = requestTimeout)
                           (f: Try[Response] => T)(implicit testContext: TestContext): T = {
    f(buildRequest(Method.Head, path, content, headers, timeout))
  }

  protected def testPatch[T](path: String, content: Option[Content] = None, headers: Map[HeaderName, String] = Map(),
                             timeout: Duration = requestTimeout)
                            (f: Try[Response] => T)(implicit testContext: TestContext): T = {
    f(buildRequest(Method.Patch, path, content, headers, timeout))
  }

}
