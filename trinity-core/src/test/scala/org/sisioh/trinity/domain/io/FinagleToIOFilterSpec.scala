package org.sisioh.trinity.domain.io

import java.net.InetSocketAddress

import com.twitter.finagle.Service
import com.twitter.finagle.builder.{ClientBuilder, Server, ServerBuilder}
import com.twitter.finagle.http.{Http, Request, RichHttp}
import com.twitter.util.{Await, Future}
import org.sisioh.trinity.domain.io.http.{Request => IORequest, Response => IOResponse}

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
