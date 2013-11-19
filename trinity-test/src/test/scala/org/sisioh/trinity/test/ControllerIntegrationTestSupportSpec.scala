package org.sisioh.trinity.test

import org.sisioh.trinity.domain.io.http.Method._
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.server.Server
import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ControllerIntegrationTestSupportSpec extends Specification with ControllerIntegrationTestSupport {

  def helloWorld = SimpleAction {
    request =>
      Future.successful(Response().withContentAsString("Hello World!"))
  }

  val routingFilter = RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> helloWorld
      )
  }

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
  }
}
