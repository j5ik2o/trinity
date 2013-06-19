package org.sisioh.trinity.test

import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.util.{Await, Future}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.jboss.netty.util.CharsetUtil.UTF_8
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain._
import org.specs2.mutable.Specification
import scala.collection.Map
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
import org.sisioh.trinity.domain.routing.RouteRepositoryOnMemory
import org.sisioh.trinity.domain.controller.{Controller, GlobalSetting, SimpleController, ControllerService}
import org.sisioh.trinity.domain.config.Config

class MockResponse(val originalResponse: FinagleResponse) {

  def status = originalResponse.getStatus()

  def code = originalResponse.getStatus().getCode

  def body = originalResponse.getContent().toString(UTF_8)

  def getHeader(name: String) = originalResponse.getHeader(name)

  def getHeaders = originalResponse.getHeaders()

}

class MockApplication(val config: Config = Config(), val routeRepository: RouteRepositoryOnMemory = new RouteRepositoryOnMemory, val statsReceiver: StatsReceiver = NullStatsReceiver)
  extends TrinityApplication{
  def registerController(controller: Controller) {}

  def start() {}

  def shutdown() {}
}

abstract class SpecHelper extends Specification {

  sequential

  def response = new MockResponse(Await.result(lastResponse))

  var lastResponse: Future[FinagleResponse] = null

  def buildRequest(method: HttpMethod, path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map()) {

    val request = FinagleRequest(path, params.toList: _*)
    request.httpRequest.setMethod(method)

    headers.foreach {
      header =>
        request.httpRequest.setHeader(header._1, header._2)
    }

    val service = new ControllerService(new MockApplication(), globalSetting)

    lastResponse = service(request)
  }

  def controller: SimpleController

  def globalSetting: Option[GlobalSetting] = None

  def get(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map()) {
    buildRequest(HttpMethod.GET, path, params, headers)
  }

  def post(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map()) {
    buildRequest(HttpMethod.POST, path, params, headers)
  }

  def put(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map()) {
    buildRequest(HttpMethod.PUT, path, params, headers)
  }

  def delete(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map()) {
    buildRequest(HttpMethod.DELETE, path, params, headers)
  }

  def head(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map()) {
    buildRequest(HttpMethod.HEAD, path, params, headers)
  }

  def patch(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map()) {
    buildRequest(HttpMethod.PATCH, path, params, headers)
  }

}
