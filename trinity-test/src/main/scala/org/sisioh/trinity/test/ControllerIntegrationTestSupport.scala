package org.sisioh.trinity.test

import com.twitter.finagle.http.{Request => FinagleRequest, Response, Http}
import org.sisioh.trinity.application.TrinityApplication
import com.twitter.util.Await
import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import java.net.{SocketAddress, InetSocketAddress}
import org.jboss.netty.handler.codec.http.{HttpResponse, HttpRequest, HttpMethod}
import org.specs2.specification.Scope
import org.specs2.execute.{Result, AsResult}
import org.specs2.mutable.Around
import scala.util.Random
import org.sisioh.trinity.domain.controller.Controller

trait ControllerIntegrationTestSupport extends ControllerTestSupport {

  val basePort = 7000

  def randomPort = {
    Some(basePort + Random.nextInt(100))
  }

  def createApplicationWithRandomPort =
    TrinityApplication(MockConfig(applicationPort = randomPort))


  def buildRequestByContent
  (method: HttpMethod, path: String, content: Option[String], headers: Map[String, String])
  (implicit application: TrinityApplication, controller: Controller): MockResponse = {
    val request = FinagleRequest(path)
    request.httpRequest.setMethod(method)
    headers.foreach {
      header =>
        request.httpRequest.setHeader(header._1, header._2)
    }
    content.foreach {
      v =>
        request.contentString = v
    }
    val address: SocketAddress = new InetSocketAddress(application.config.applicationPort.getOrElse(7070))
    val client: Service[HttpRequest, HttpResponse] =
      ClientBuilder()
        .codec(Http())
        .hosts(address)
        .hostConnectionLimit(1)
        .build()
    val finagleResponse = Await.result(client(request))
    new MockResponse(Response(finagleResponse))
  }

  def buildRequestByParams
  (method: HttpMethod,
   path: String,
   params: Map[String, String] = Map(),
   headers: Map[String, String] = Map())
  (implicit application: TrinityApplication, controller: Controller): MockResponse = withDebugScope(s"buildRequest($path)") {
    val request = FinagleRequest(path, params.toList: _*)
    request.httpRequest.setMethod(method)
    headers.foreach {
      header =>
        request.httpRequest.setHeader(header._1, header._2)
    }
    val address: SocketAddress = new InetSocketAddress(application.config.applicationPort.getOrElse(7070))
    val client: Service[HttpRequest, HttpResponse] =
      ClientBuilder()
        .codec(Http())
        .hosts(address)
        .hostConnectionLimit(1)
        .build()
    val finagleResponse = Await.result(client(request))
    new MockResponse(Response(finagleResponse))
  }

  class WithServer(application: TrinityApplication, controller: Controller)
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
