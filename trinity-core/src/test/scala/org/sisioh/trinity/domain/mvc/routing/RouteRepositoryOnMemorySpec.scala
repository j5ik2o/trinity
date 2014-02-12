package org.sisioh.trinity.domain.mvc.routing

import org.specs2.mutable.Specification
import org.sisioh.trinity.domain.io.http.Methods
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{PathPattern, SinatraPathPatternParser}
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.http.ResponseSupport

class RouteRepositoryOnMemorySpec extends Specification with ResponseSupport {

  val repository = RouteRepositoryOnMemory()

  val action = SimpleAction {
    _ => responseBuilder.toFuture
  }

  "repository" should {
    "find route" in {
      val p = SinatraPathPatternParser()

      val route1 = Route(RouteId(Methods.Get, PathPattern("/a(.*)".r)), action)
      val route2 = Route(RouteId(Methods.Get, p("/a")), action)

      repository.store(route1)
      repository.store(route2)

      val routeOpt1 = repository.find {
        case Route(RouteId(m, pattern), _) =>
          pattern("/a").isDefined
      }
      routeOpt1 must beSome(route1)
    }
  }


}
