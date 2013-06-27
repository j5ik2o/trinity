package org.sisioh.trinity.test

import com.twitter.finagle.SimpleFilter
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
import com.twitter.util.Await
import java.io.File
import org.jboss.netty.handler.codec.http.HttpMethod
import org.jboss.netty.util.CharsetUtil.UTF_8
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.config.{Environment, Config}
import org.sisioh.trinity.domain.controller._
import org.sisioh.trinity.domain.routing.RouteRepositoryOnMemory
import scala.collection.Map
import scala.concurrent.duration.Duration

class MockResponse(val originalResponse: FinagleResponse) {

  def status = originalResponse.getStatus()

  def code = originalResponse.getStatus().getCode

  def body = originalResponse.getContent().toString(UTF_8)

  def getHeader(name: String) = originalResponse.getHeader(name)

  def getHeaders = originalResponse.getHeaders()

}

case class MockConfig
(environment: Environment.Value = Environment.Development,
 applicationName: String = "TestApplication",
 applicationPort: Option[Int] = None,
 statsEnabled: Boolean = false,
 statsPort: Option[Int] = None,
 templateWorkDir: File = new File("./temp"),
 templatePath: String = "/",
 localDocumentRoot: String = "src/test/resources",
 maxRequestSize: Option[Int] = None,
 maxResponseSize: Option[Int] = None,
 maxConcurrentRequests: Option[Int] = None,
 hostConnectionMaxIdleTime: Option[Duration] = None,
 hostConnectionMaxLifeTime: Option[Duration] = None,
 requestTimeout: Option[Int] = None) extends Config

case class MockApplication
(config: Config = MockConfig(),
 routeRepository: RouteRepositoryOnMemory = new RouteRepositoryOnMemory,
 controllerRepository: ControllerRepository = new ControllerRepositoryOnMemory,
 statsReceiver: StatsReceiver = NullStatsReceiver)
  extends TrinityApplication {

  def registerController(controller: Controller) = synchronized {
    controllerRepository.store(controller)
    controller.routeRepository.foreach {
      e =>
        routeRepository.store(e).get
    }
  }

  def registerFilter(filter: SimpleFilter[FinagleRequest, FinagleResponse]) {
  }

  def start() {}

  def shutdown() {}

}

trait ControllerTestSupport {

  def buildRequest
  (method: HttpMethod,
   path: String,
   params: Map[String, String] = Map(),
   headers: Map[String, String] = Map())
  (implicit application: TrinityApplication) = {
    val request = FinagleRequest(path, params.toList: _*)
    request.httpRequest.setMethod(method)
    headers.foreach {
      header =>
        request.httpRequest.setHeader(header._1, header._2)
    }
    application.registerController(controller)
    val service = new ControllerService(application, globalSetting)
    val finagleResponse = Await.result(service(request))
    new MockResponse(finagleResponse)
  }

  def controller: SimpleController

  def globalSetting: Option[GlobalSettings] = None

  def testGet(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
             (f: MockResponse => Unit)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.GET, path, params, headers))
  }

  def testPost(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
              (f: MockResponse => Unit)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.POST, path, params, headers))
  }

  def testPut(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
             (f: MockResponse => Unit)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.PUT, path, params, headers))
  }

  def testDelete(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
                (f: MockResponse => Unit)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.DELETE, path, params, headers))
  }

  def testHead(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
              (f: MockResponse => Unit)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.HEAD, path, params, headers))
  }

  def testPatch(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())
               (f: MockResponse => Unit)(implicit application: TrinityApplication) = {
    f(buildRequest(HttpMethod.PATCH, path, params, headers))
  }

}
