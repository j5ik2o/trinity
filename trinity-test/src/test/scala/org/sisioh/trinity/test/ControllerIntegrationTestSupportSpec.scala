package org.sisioh.trinity.test

import org.sisioh.trinity.domain.io.http.Methods._
import org.sisioh.trinity.domain.mvc.Environment
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.http.ResponseBuilder
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.server.Server
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

  val routingFilter = RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> helloWorld
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

  }
}
