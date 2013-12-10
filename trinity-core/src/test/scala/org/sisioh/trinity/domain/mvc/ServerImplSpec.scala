package org.sisioh.trinity.domain.mvc

import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.{Request => FinagleRequest, Http, RichHttp}
import com.twitter.util.{Await => TAwait}
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit
import org.junit.runner.RunWith
import org.sisioh.trinity.domain.io.http.Charsets
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.server.{ServerConfig, Server}
import org.specs2.execute.{Result, AsResult}
import org.specs2.mutable.{Around, Specification}
import org.specs2.runner.JUnitRunner
import org.specs2.specification.Scope
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await => SAwait, Future}

@RunWith(classOf[JUnitRunner])
class ServerImplSpec extends Specification {

  sequential

  class Setup(action: Option[Action[Request, Response]] = None)
    extends Around with Scope {

    val client = ClientBuilder()
      .codec(RichHttp[FinagleRequest](Http()))
      .hosts(new InetSocketAddress(8080))
      .hostConnectionLimit(1)
      .build()

    private def running[T](server: Server)(block: => T): T = {
      synchronized {
        try {
          SAwait.result(server.start(Environment.Development), Duration(3, TimeUnit.SECONDS))
          block
        } finally {
          SAwait.result(server.stop, Duration(3, TimeUnit.SECONDS))
          Thread.sleep(100)
        }
      }
    }

    def around[T: AsResult](t: => T): Result = {
      val server = Server(ServerConfig(), action = action, filter = None, globalSettings = None)
      running(server)(AsResult(t))
    }

  }

  val body = "TEST"

  "server" should {
    "404 NOT FOUND" in new Setup(None) {
      val responseFuture = client(FinagleRequest())
      TAwait.result(responseFuture).getStatusCode() must_== 404
      TAwait.result(responseFuture).getContent().toString(Charsets.UTF_8.toObject) must_== ""
    }
    "200 OK" in new Setup(
      Some(
        new Action[Request, Response] {
          def apply(request: Request): Future[Response] = {
            println("start action")
            Future.successful {
              Response().withContentAsString(body)
            }
          }
        }
      )
    ) {
      val responseFuture = client(FinagleRequest())
      TAwait.result(responseFuture).getStatusCode() must_== 200
      TAwait.result(responseFuture).getContent().toString(Charsets.UTF_8.toObject) must_== body
    }
  }

}
