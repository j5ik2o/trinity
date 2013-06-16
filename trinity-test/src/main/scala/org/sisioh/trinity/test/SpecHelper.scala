package org.sisioh.trinity.test

import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpMethod
import org.jboss.netty.util.CharsetUtil.UTF_8
import org.sisioh.trinity.{GlobalSetting, Controller, ControllerService, Controllers}
import scala.collection.Map
import org.specs2.mutable.Specification

class MockResponse(val originalResponse: FinagleResponse) {

  def status = originalResponse.getStatus

  def code = originalResponse.getStatus.getCode

  def body = originalResponse.getContent.toString(UTF_8)

  def getHeader(name: String) = originalResponse.getHeader(name)

  def getHeaders = originalResponse.getHeaders

}

abstract class SpecHelper extends Specification {

  sequential

  def response = new MockResponse(lastResponse.get)

  var lastResponse: Future[FinagleResponse] = null

  def buildRequest(method: HttpMethod, path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map()) {

    val request = FinagleRequest(path, params.toList: _*)
    request.httpRequest.setMethod(method)

    headers.foreach {
      header =>
        request.httpRequest.setHeader(header._1, header._2)
    }

    val collection = new Controllers
    collection.add(controller)

    val service = new ControllerService(collection, globalSetting)

    lastResponse = service(request)
  }

  def controller: Controller

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
