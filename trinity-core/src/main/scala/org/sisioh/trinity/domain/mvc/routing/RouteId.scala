package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.dddbase.core.model.Identity
import org.sisioh.trinity.domain.io.http.Methods
import org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPattern

/**
 * [[org.sisioh.trinity.domain.mvc.routing.Route]]のための識別子。
 *
 * @param method
 * @param pathPattern
 */
case class RouteId(method: Methods.Value, pathPattern: PathPattern)
  extends Identity[(Methods.Value, PathPattern)] {
  def value: (Methods.Value, PathPattern) = (method, pathPattern)
}

