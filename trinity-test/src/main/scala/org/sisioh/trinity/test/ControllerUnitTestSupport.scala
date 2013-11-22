package org.sisioh.trinity.test

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Response => FinagleResponse, Request => FinagleRequest}
import com.twitter.util.Await
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.domain.io.http.{Response => IOResponse, HeaderName}
import org.sisioh.trinity.domain.mvc.GlobalSettings
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Request, Response}
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.server.ServiceBuilder
import scala.concurrent.ExecutionContext
import scala.util.Try

/**
 * 単体テストをサポートするためのトレイト。
 */
trait ControllerUnitTestSupport extends ControllerTestSupport {
  self =>

  val routingFilter: RoutingFilter

  protected def buildRequest
  (method: HttpMethod,
   path: String,
   content: Option[Content],
   headers: Map[HeaderName, String])
  (implicit executor: ExecutionContext): Try[Response] = {
    val request = newRequest(method, path, content, headers)
    val serviceBuilder = new ServiceBuilder {
      val globalSettings: Option[GlobalSettings[Request, Response]] = self.globalSettings

      def _buildService(action: Option[Action[Request, Response]] = None)
                       (implicit executor: ExecutionContext): Service[FinagleRequest, FinagleResponse] =
        buildService(action)(executor)
    }
    serviceBuilder.registerFilter(routingFilter)
    val service = serviceBuilder._buildService()
    Try {
      val finagleResponse = Await.result(service(request))
      val r = IOResponse(finagleResponse)
      Response(r)
    }
  }


}
