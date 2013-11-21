package org.sisioh.trinity.test

import com.twitter.finagle.http.{Response => FinagleResponse, Request => FinagleRequest}
import com.twitter.finagle.{Filter => FinagleFilter}
import com.twitter.util.Await
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.domain.io.http.{Response => IOResponse, HeaderName}
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.server.ServiceBuilder
import scala.concurrent.ExecutionContext
import scala.util.Try

/**
 * 単体テストをサポートするためのトレイト。
 */
trait ControllerUnitTestSupport extends ControllerTestSupport with ServiceBuilder {

  val routingFilter: RoutingFilter

  protected def buildRequest
  (method: HttpMethod,
   path: String,
   content: Option[Content],
   headers: Map[HeaderName, String])
  (implicit executor: ExecutionContext): Try[Response] = {
    val request = newRequest(method, path, content, headers)
    registerFilter(routingFilter)
    val service = buildService()
    Try {
      val finagleResponse = Await.result(service(request))
      val r = IOResponse(finagleResponse)
      Response(r)
    }
  }


}
