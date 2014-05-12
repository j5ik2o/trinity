/*
 * Copyright 2013 Sisioh Project and others. (http://sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.trinity.domain.io.http.Methods
import org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPattern

/**
 * [[org.sisioh.trinity.domain.mvc.routing.Route]]のための識別子。
 *
 * @param method
 * @param pathPattern
 */
case class RouteId(method: Methods.Value, pathPattern: PathPattern) {

  /**
   * Gets the method and the pathPattern.
   *
   * @return method and pathPattern
   */
  def value: (Methods.Value, PathPattern) = (method, pathPattern)

}

