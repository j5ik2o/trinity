package org.sisioh.trinity.test

import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.{Http, Response => FinagleResponse}
import com.twitter.util.{Await => TAwait}
import java.net.{InetSocketAddress, SocketAddress}
import java.util.concurrent.TimeUnit
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpRequest, HttpResponse}
import org.sisioh.trinity.domain.io.http.{HeaderName, Response => IOResponse}
import org.sisioh.trinity.domain.mvc.Environment
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.server.Server
import org.sisioh.trinity.util.DurationConverters._
import org.specs2.execute.{AsResult, Result}
import org.specs2.mutable.Around
import org.specs2.specification.Scope
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Await => SAwait}
import scala.util.Try

/**
 * インテグレーションテストをサポートするためのトレイト。
 */
trait ControllerIntegrationTestSupport extends ControllerTestSupport {

  case class IntegrationTestContext(server: TestServer = TestServer())
                                   (implicit val executor: ExecutionContext) extends TestContext

  private val httpClients = scala.collection.mutable.Map.empty[SocketAddress, Service[HttpRequest, HttpResponse]]



  protected val serverAwaitDuration = Duration(5, TimeUnit.MINUTES)

  protected def buildRequest
  (method: HttpMethod, path: String, content: Option[Content], headers: Map[HeaderName, String], timeout: Duration)
  (implicit testContext: TestContext): Try[Response] = {
    implicit val executor = testContext.executor
    val request = newRequest(method, path, content, headers)
    val host = testContext.server.host.getOrElse(defaultHost)
    val port = testContext.server.port.getOrElse(defaultPort)
    val address: SocketAddress = new InetSocketAddress(host, port)
    val client = httpClients.getOrElseUpdate(
      address,
      ClientBuilder()
        .codec(Http())
        .hosts(address)
        .hostConnectionLimit(1)
        .build()
    )
    Try {
      val finagleResponse = TAwait.result(client(request), timeout.toTwitter)
      val r = IOResponse(FinagleResponse(finagleResponse))
      Response(r)
    }
  }

  protected class WithServer(server: Server,
                             environment: Environment.Value = Environment.Product,
                             awaitDuration: Duration = serverAwaitDuration)
                            (implicit executor: ExecutionContext)
    extends Around with Scope {

    private def running[T](block: => T): T = {
      synchronized {
        val future = server.start(environment).map {
          _ =>
            block
        }.flatMap {
          result =>
            server.stop().map(_ => result)
        }
        SAwait.result(future, awaitDuration)
      }
    }

    def around[T: AsResult](t: => T): Result = {
      running(AsResult.effectively(t))
    }

  }

}
