package org.sisioh.trinity.test

import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.controller.{Controller, ControllerService}
import com.twitter.util.Await

trait ControllerUnitTestSupport extends ControllerTestSupport {

  def buildRequestByContent
  (method: HttpMethod,
   path: String,
   content: Option[String],
   headers: Map[String, String])
  (implicit application: TrinityApplication, controller: Controller): MockResponse = {
    val request = FinagleRequest(path)
    content.foreach{
      v =>
        request.contentString = v
    }
    request.httpRequest.setMethod(method)
    headers.foreach {
      header =>
        request.httpRequest.setHeader(header._1, header._2)
    }
    application.registerController(controller)
    val service = new ControllerService(application, getGlobalSettings)
    val finagleResponse = Await.result(service(request))
    new MockResponse(finagleResponse)
  }

  def buildRequestByParams
  (method: HttpMethod,
   path: String,
   params: Map[String, String] = Map(),
   headers: Map[String, String] = Map())
  (implicit application: TrinityApplication, controller: Controller): MockResponse = {
    val request = FinagleRequest(path, params.toList: _*)
    request.httpRequest.setMethod(method)
    headers.foreach {
      header =>
        request.httpRequest.setHeader(header._1, header._2)
    }
    application.registerController(controller)
    val service = new ControllerService(application, getGlobalSettings)
    val finagleResponse = Await.result(service(request))
    new MockResponse(finagleResponse)
  }

}
