package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.trinity.domain.io.transport.codec.http.Method
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.controller.Controller
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{PathPatternParser, PathPattern}
import scala.concurrent.Future

object RouteDsl {

  case class ControllerDef(pathDef: PathPatternDef, controller: Controller) {

    def %(action: Action[Request, Response]): RouteDef =
      RouteDef(pathDef.method, pathDef.pathPattern, controller, action)

    def %(action: (Request) => Future[Response]): RouteDef =
      RouteDef(pathDef.method, pathDef.pathPattern, controller, Action(action))

  }

  case class PathPatternDef(method: Method.Value, pathPattern: PathPattern) {

    def %(controller: Controller) =
      ControllerDef(this, controller)

  }

  implicit class MethodDef(val method: Method.Value) extends AnyVal {

    def %(path: String)(implicit pathPatternParser: PathPatternParser) =
      PathPatternDef(method, pathPatternParser(path))

    def %(pathPattern: PathPattern) =
      PathPatternDef(method, pathPattern)
  }


}
