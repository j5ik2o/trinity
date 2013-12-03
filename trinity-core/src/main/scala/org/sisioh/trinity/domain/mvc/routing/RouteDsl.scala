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
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Response, Request}
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{PathPatternParser, PathPattern}
import scala.concurrent.Future
import scala.util.matching.Regex

object RouteDsl {

  case class PathPatternDef(method: Methods.Value, pathPattern: PathPattern) {

    def ->(action: Action[Request, Response]): RouteDef =
      RouteDef(method, pathPattern, action)

    def ->(action: (Request) => Future[Response]): RouteDef =
      RouteDef(method, pathPattern, Action(action))

  }

  implicit class MethodDef(val method: Methods.Value) extends AnyVal {

    def %(path: String)(implicit pathPatternParser: PathPatternParser): PathPatternDef =
      PathPatternDef(method, pathPatternParser(path))

    def %(pathPattern: (Regex, Seq[String])): PathPatternDef =
      PathPatternDef(method, PathPattern(pathPattern._1, pathPattern._2))

    def %(pathPattern: PathPattern): PathPatternDef =
      PathPatternDef(method, pathPattern)
  }


}
