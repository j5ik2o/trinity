package org.sisioh.trinity.test

import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.{Request => FinagleRequest, Response => FinagleResponse, Http}
import com.twitter.util.{Await => TAwait}
import java.net.{SocketAddress, InetSocketAddress}
import org.jboss.netty.handler.codec.http.{HttpResponse, HttpRequest, HttpMethod}
import org.sisioh.trinity.domain.io.http.{Response => IOResponse}
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.server.Server
import org.specs2.execute.{Result, AsResult}
import org.specs2.mutable.Around
import org.specs2.specification.Scope
import scala.concurrent.duration.Duration
import scala.concurrent.{Await => SAwait, ExecutionContext}
import scala.util.Try

/**
 * インテグレーションテストをサポートするためのトレイト。
 */
trait ControllerIntegrationTestSupport extends ControllerTestSupport {

  protected def buildRequest
  (method: HttpMethod, path: String, content: Option[Content], headers: Map[String, String])
  (implicit executor: ExecutionContext): Try[Response] = {
    val request = newRequest(method, path, content, headers)
    val address: SocketAddress = new InetSocketAddress(serverHost.getOrElse("localhost"), serverPort.getOrElse(7070))
    val client: Service[HttpRequest, HttpResponse] =
      ClientBuilder()
        .codec(Http())
        .hosts(address)
        .hostConnectionLimit(1)
        .build()
    Try {
      val finagleResponse = TAwait.result(client(request))
      val r = IOResponse(FinagleResponse(finagleResponse))
      Response(r)
    }
  }

  protected class WithServer(server: Server)(implicit executor: ExecutionContext)
    extends Around with Scope {

    private def running[T](block: => T): T = {
      synchronized {
        val future = server.start().map {
          _ =>
            block
        }.flatMap {
          result =>
            server.stop().map(_ => result)
        }
        SAwait.result(future, Duration.Inf)
      }
    }

    def around[T: AsResult](t: => T): Result = {
      running(AsResult(t))
    }

  }

}
