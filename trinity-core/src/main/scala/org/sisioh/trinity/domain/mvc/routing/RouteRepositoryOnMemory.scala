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

import org.sisioh.trinity.domain.mvc.http.Request
import org.sisioh.trinity.domain.mvc.http.Response

private[routing]
case class RouteRepositoryOnMemory()
  extends RouteRepository {

  private val entites: collection.mutable.Map[RouteId, Route[Request, Response]] =
    collection.mutable.Map.empty[RouteId, Route[Request, Response]]

  override def store(route: Route[Request, Response]): Unit = {
    entites += (route.identifier -> route)
  }

  override def find(predicate: PartialFunction[Route[Request, Response], Boolean]): Option[Route[Request, Response]] = {
    entites.map(_._2).toSeq.sorted.find {
      route => predicate(route)
    }
  }

}
