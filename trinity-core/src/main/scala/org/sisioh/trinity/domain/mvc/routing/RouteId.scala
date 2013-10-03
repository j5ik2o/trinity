package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.dddbase.core.model.Identity
import org.sisioh.trinity.domain.io.transport.codec.http.Method

/**
 * [[org.sisioh.trinity.domain.mvc.routing.Route]]のための識別子。
 *
 * @param method
 * @param pathPattern
 */
case class RouteId(method: Method.Value, pathPattern: PathPattern)
  extends Identity[(Method.Value, PathPattern)] {
  def value: (Method.Value, PathPattern) = (method, pathPattern)
}

