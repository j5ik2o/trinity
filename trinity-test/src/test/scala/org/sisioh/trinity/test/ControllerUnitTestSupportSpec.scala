package org.sisioh.trinity.test

import org.sisioh.trinity.domain.io.http.Methods._
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.http.ResponseBuilder
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global

class ControllerUnitTestSupportSpec extends Specification with ControllerUnitTestSupport {

  def helloWorld = SimpleAction {
    request =>
      ResponseBuilder().withContent("Hello World!").toFuture
  }


  "unit-test" should {
    "test get method" in new WithTestScope {
      val routingFilter = RoutingFilter.createForActions {
        implicit pathPatternParser =>
          Seq(
            Get % "/hello" -> helloWorld
          )
      }
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
