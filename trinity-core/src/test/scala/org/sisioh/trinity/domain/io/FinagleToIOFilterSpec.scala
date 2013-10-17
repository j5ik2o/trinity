package org.sisioh.trinity.domain.io

import com.twitter.finagle.builder.{ ClientBuilder, ServerBuilder, Server }
import com.twitter.finagle.http.{ Request, RichHttp, Http }
import java.net.InetSocketAddress
import org.sisioh.trinity.domain.io.http.{ Request => IORequest, Response => IOResponse }
import com.twitter.finagle.Service
import com.twitter.util.{ Await, Future }
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
object FinagleToIOFilterSpec extends App {

  case class Respond(response: IOResponse) extends Service[IORequest, IOResponse] {
    def apply(request: IORequest) = Future.value(response)
  }

  val response = IOResponse()
  val service = FinagleToIOFilter() andThen Respond(response)

  val server: Server = ServerBuilder()
    .codec(RichHttp[Request](Http()))
    .bindTo(new InetSocketAddress(8080))
    .name("httpserver")
    .build(service)

  val client = ClientBuilder()
    .codec(RichHttp[Request](Http()))
    .hosts(new InetSocketAddress(8080))
    .hostConnectionLimit(1)
    .build()

  val request = Request()

  val future = client(request)
  val response2 = Await.result(future)
  println(response2)
}
