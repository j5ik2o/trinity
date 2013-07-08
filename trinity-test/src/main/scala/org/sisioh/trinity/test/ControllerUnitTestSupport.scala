package org.sisioh.trinity.test

import com.twitter.util.Await
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.controller.{Controller, ControllerService}

/**
 * 単体テストをサポートするためのトレイト。
 */
trait ControllerUnitTestSupport extends ControllerTestSupport {

  protected def buildRequest
  (method: HttpMethod,
   path: String,
   content: Option[Content],
   headers: Map[String, String])
  (implicit application: TrinityApplication, controller: Controller): MockResponse = {
    val request = newRequest(method, path, content, headers)
    application.registerController(controller)
    val service = new ControllerService(application, getGlobalSettings)
    val finagleResponse = Await.result(service(request))
    new MockResponse(finagleResponse)
  }


}
