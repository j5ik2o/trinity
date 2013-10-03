package org.sisioh.trinity.domain.mvc

import org.specs2.mutable.{Around, Specification}
import scala.concurrent.{Await => SAwait, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import org.sisioh.trinity.domain.io.buffer.ChannelBuffers
import org.sisioh.trinity.domain.io.transport.codec.http.CharsetUtil
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.{Request => FinagleRequest, Http, RichHttp}
import java.net.InetSocketAddress
import org.specs2.specification.Scope
import org.specs2.execute.{Result, AsResult}
import scala.concurrent.duration.Duration
import com.twitter.util.{Await => TAwait}

class ServerImplSpec extends Specification {

  sequential

  class Setup(actionOpt: Option[Action[Request, Response]] = None)
    extends Around with Scope {

    val client = ClientBuilder()
      .codec(RichHttp[FinagleRequest](Http()))
      .hosts(new InetSocketAddress(8080))
      .hostConnectionLimit(1)
      .build()

    private def running[T](server: Server)(block: => T): T = {
      synchronized {
        try {
          SAwait.result(server.start, Duration.Inf)
          block
        } finally {
          SAwait.result(server.stop, Duration.Inf)
          Thread.sleep(100)
        }
      }
    }

    def around[T: AsResult](t: => T): Result = {
      val server = new ServerImpl(ServerConfig(), actionOpt = actionOpt, globalSettingsOpt = None)
      running(server)(AsResult(t))
    }

  }

  val body = "TEST"

  "server" should {
    "404 NOT FOUND" in new Setup(None) {
      val responseFuture = client(FinagleRequest())
      TAwait.result(responseFuture).getStatusCode() must_== 404
      TAwait.result(responseFuture).getContent().toString(CharsetUtil.UTF_8) must_== ""
    }
    "200 OK" in new Setup(
      Some(
        new Action[Request, Response] {
          def apply(request: Request): Future[Response] =
            Future.successful {
              Response().withContent(ChannelBuffers.copiedBuffer(body, CharsetUtil.UTF_8))
            }
        }
      )
    ) {
      val responseFuture = client(FinagleRequest())
      TAwait.result(responseFuture).getStatusCode() must_== 200
      TAwait.result(responseFuture).getContent().toString(CharsetUtil.UTF_8) must_== body
    }
  }

}
