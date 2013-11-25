package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.trinity.domain.io.http.Methods
import org.sisioh.trinity.domain.mvc.http.{Request, Response}
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPattern

case class RouteDef
(method: Methods.Value,
 pathPattern: PathPattern,
 action: Action[Request, Response])


