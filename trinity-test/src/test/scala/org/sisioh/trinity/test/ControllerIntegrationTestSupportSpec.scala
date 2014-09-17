package org.sisioh.trinity.test

import java.net.InetSocketAddress
import org.sisioh.trinity.domain.io.http.Methods._
import org.sisioh.trinity.domain.mvc.Environment
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.http.ResponseBuilder
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.server.{Server, ServerConfig}
import org.specs2.mutable.Specification
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class ControllerIntegrationTestSupportSpec extends Specification with ControllerIntegrationTestSupport {

  sequential

  def helloWorld = SimpleAction {
    request =>
      ResponseBuilder().withContent("Hello World!").toFuture
  }

  def post = SimpleAction {
    request =>
      ResponseBuilder().withContent("body = " + request.contentAsString()).toFuture
  }

  val routingFilter = RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> helloWorld,
        Post % "/post" -> post
      )
  }

  implicit val testContext = IntegrationTestContext()

  "integration-test" should {
    "test get method" in new WithServer(Server(filter = Some(routingFilter))) {
      testGet("/hello") {
        result =>
          result must beSuccessfulTry.like {
            case response =>
              response.contentAsString() must_== "Hello World!"
          }
      }

    }
    "test post method" in new WithServer(Server(filter = Some(routingFilter))){
      testPost("/post", Some(StringContent("hoge"))) {
        result =>
          result must beSuccessfulTry.like {
            case response =>
              response.contentAsString() must_== "body = hoge"
          }
      }
    }
    "test post method" in new WithServer(Server(filter = Some(routingFilter))){
      testPost("/post", Some(MapContent(Map("hoge"->"fuga")))) {
        result =>
          result must beSuccessfulTry.like {
            case response =>
              response.contentAsString() must_== "body = hoge=fuga"
          }
      }
    }
    "test get method without WithServer Scope" in {
      val server = Server(filter = Some(routingFilter))
      val f = server.start(Environment.Development).map {
        _ =>
          testGet("/hello") {
            result =>
              result must beSuccessfulTry.like {
                case response =>
                  response.contentAsString() must_== "Hello World!"
              }
          }
      }.flatMap {
        result =>
          server.stop.map(_ => result)
      }
      Await.result(f, Duration.Inf)
    }
    "test get method by specifying the server" in {
      val bindAddress = new InetSocketAddress("localhost", 17070)
      val serverConf = ServerConfig(bindAddress = Option(bindAddress))
      val testServer = TestServer(host = Option(bindAddress.getHostName), port = Option(bindAddress.getPort))
      implicit val testContext = IntegrationTestContext(server = testServer)
      val server = Server(serverConfig = serverConf, filter = Some(routingFilter))
      val f = server.start(Environment.Development).map {
        _ =>
          testGet("/hello") {
            result =>
              result must beSuccessfulTry.like {
                case response =>
                  response.contentAsString() must_== "Hello World!"
              }
          }
      }.flatMap {
        result =>
          server.stop.map(_ => result)
      }
      Await.result(f, Duration.Inf)
    }

  }
}
