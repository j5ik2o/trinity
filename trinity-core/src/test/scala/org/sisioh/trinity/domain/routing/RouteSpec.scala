package org.sisioh.trinity.domain.routing

import org.specs2.mutable.Specification
import com.twitter.finagle.http.Method
import org.sisioh.trinity.domain.controller.AbstractController
import org.sisioh.trinity.application.TrinityApplication
import org.specs2.mock.Mockito

class RouteSpec extends Specification with Mockito {

  implicit val application = mock[TrinityApplication]
  implicit val pathParser = new SinatraPathPatternParser()

  object MockController extends AbstractController {
    def index = FutureAction {
      request =>
        responseBuilder.withOk.toFinagleResponseFuture
    }
  }

  "route" should {
    "be greater than old other one." in {
      val route1 = Route(Method.Get, "/", MockController, MockController.index)
      val route2 = Route(Method.Get, "/", MockController, MockController.index)
      route1 must be < route2
    }
  }


}
