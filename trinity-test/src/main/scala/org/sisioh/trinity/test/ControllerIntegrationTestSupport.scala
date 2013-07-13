package org.sisioh.trinity.test

import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.{Request => FinagleRequest, Response, Http}
import com.twitter.util.Await
import java.net.{SocketAddress, InetSocketAddress}
import org.jboss.netty.handler.codec.http.{HttpResponse, HttpRequest, HttpMethod}
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.controller.Controller
import org.specs2.execute.{Result, AsResult}
import org.specs2.mutable.Around
import org.specs2.specification.Scope
import scala.util.{Try, Random}

/**
 * インテグレーションテストをサポートするためのトレイト。
 */
trait ControllerIntegrationTestSupport extends ControllerTestSupport {

  protected val basePort = 7000

  protected def randomPort = {
    Some(basePort + Random.nextInt(100))
  }

  protected def createApplicationWithRandomPort =
    TrinityApplication(MockConfig(applicationPort = randomPort))


  protected def buildRequest
  (method: HttpMethod, path: String, content: Option[Content], headers: Map[String, String])
  (implicit application: TrinityApplication, controller: Controller): Try[MockResponse] = {
    val request = newRequest(method, path, content, headers)
    val address: SocketAddress = new InetSocketAddress(application.config.applicationPort.getOrElse(7070))
    val client: Service[HttpRequest, HttpResponse] =
      ClientBuilder()
        .codec(Http())
        .hosts(address)
        .hostConnectionLimit(1)
        .build()
    Try {
      val finagleResponse = Await.result(client(request))
      new MockResponse(Response(finagleResponse))
    }
  }

  protected class WithServer(implicit application: TrinityApplication, controller: Controller)
    extends Around with Scope {

    private def running[T](application: TrinityApplication)(block: => T): T = {
      synchronized {
        try {
          application.registerController(controller)
          application.start()
          block
        } finally {
          application.shutdown()
          Thread.sleep(100)
        }
      }
    }

    def around[T: AsResult](t: => T): Result = {
      running(application)(AsResult(t))
    }

  }

}
