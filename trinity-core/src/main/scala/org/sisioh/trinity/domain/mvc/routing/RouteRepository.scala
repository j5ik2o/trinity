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

import org.sisioh.trinity.domain.mvc.http.{Response, Request}

/**
 * Represents the repository trait for [[Route]].
 */
trait RouteRepository {

  /**
   * Finds a route by the partial-function.
   *
   * @param function partial-function
   * @return wrapped [[Route]] around `scala.Option`
   */
  def find(function: PartialFunction[Route[Request, Response], Boolean]): Option[Route[Request, Response]]

  /**
   * Stores a route.
   *
   * @param route [[Route]]
   */
  def store(route: Route[Request, Response]): Unit

}

/**
 * Represents the companion object for [[Route]].
 */
object RouteRepository {

  /**
   * Creates a instance of [[RouteRepository]] on Memory.
   *
   * @return [[RouteRepository]]
   */
  def ofMemory: RouteRepository = RouteRepositoryOnMemory()

}
