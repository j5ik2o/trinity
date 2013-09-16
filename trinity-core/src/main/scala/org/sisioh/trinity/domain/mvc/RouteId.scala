package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.transport.codec.http.Method
import org.sisioh.dddbase.core.model.Identity

/**
 * [[org.sisioh.trinity.domain.mvc.Route]]のための識別子。
 *
 * @param method
 * @param pathPattern
 */
case class RouteId(method: Method.Value, pathPattern: PathPattern)
  extends Identity[(Method.Value, PathPattern)] {
  def value: (Method.Value, PathPattern) = (method, pathPattern)
}

