package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.trinity.domain.io.transport.codec.http.Method
import org.sisioh.trinity.domain.mvc._
import scala.concurrent.Future
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.controller.Controller

object RouteDsl {

  case class ActionDef(controllerDef: ControllerDef, action: Action[Request, Response])

  case class ControllerDef(pathDef: PathDef, controller: Controller) {
    def %(action: Action[Request, Response]) =
      ActionDef(this, action)

    def %(action: (Request) => Future[Response]) =
      ActionDef(this, Action(action))
  }

  case class PathDef(method: Method.Value, path: String) {
    def %(controller: Controller) =
      ControllerDef(this, controller)
  }

  implicit class MethodDef(val method: Method.Value) extends AnyVal {
    def %(path: String) = {
      PathDef(method, path)
    }
  }


}
