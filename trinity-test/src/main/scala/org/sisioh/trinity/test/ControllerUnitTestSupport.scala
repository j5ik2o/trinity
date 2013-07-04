package org.sisioh.trinity.test

import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.controller.ControllerService
import com.twitter.util.Await

trait ControllerUnitTestSupport extends ControllerTestSupport {

  def buildRequest
  (method: HttpMethod,
   path: String,
   params: Map[String, String] = Map(),
   headers: Map[String, String] = Map())
  (implicit application: TrinityApplication): MockResponse = {
    val request = FinagleRequest(path, params.toList: _*)
    request.httpRequest.setMethod(method)
    headers.foreach {
      header =>
        request.httpRequest.setHeader(header._1, header._2)
    }
    val controller = getController
    application.registerController(controller)
    val service = new ControllerService(application, getGlobalSettings)
    val finagleResponse = Await.result(service(request))
    new MockResponse(finagleResponse)
  }

}
