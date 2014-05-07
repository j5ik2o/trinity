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
package org.sisioh.trinity.domain.mvc.controller

import org.sisioh.trinity.domain.io.http.Methods
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.http.{Request, Response}
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{PathPattern, PathPatternParser}
import org.sisioh.trinity.domain.mvc.routing.{RouteDefHolder, RouteDef}
import scala.collection.mutable
import scala.concurrent.Future
import scala.util.matching.Regex

/**
 * Represents the trait to support scalatra like controller.
 */
trait ScalatraLikeControllerSupport extends ControllerSupport with RouteDefHolder {

  /**
   * defined values to represent routes.
   */
  protected val routeDefs = mutable.ListBuffer.empty[RouteDef]

  /**
   * Adds a route to the action to process GET request with path.
   *
   * @param path
   * @param callback
   * @param pathPatternParser
   * @return
   */
  protected def get(path: String)
                   (callback: Request => Future[Response])
                   (implicit pathPatternParser: PathPatternParser) {
    addRoute(Methods.Get, path)(callback)
  }

  /**
   * Adds a route to the action to process GET request by [[Regex]].
   *
   * @param regex
   * @param captureGroupNames
   * @param callback
   * @return
   */
  protected def get(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                   (callback: Request => Future[Response]) {
    addRoute(Methods.Get, regex, captureGroupNames)(callback)
  }

  /**
   * Adds a route to the action to process DELETE request with path.
   *
   * @param path
   * @param callback
   * @param pathPatternParser
   * @return
   */
  protected def delete(path: String)
                      (callback: Request => Future[Response])
                      (implicit pathPatternParser: PathPatternParser) {
    addRoute(Methods.Delete, path)(callback)
  }

  /**
   * Adds a route to the action to process DELETE request by [[Regex]].
   *
   * @param regex
   * @param captureGroupNames
   * @param callback
   * @return
   */
  protected def delete(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                      (callback: Request => Future[Response]) {
    addRoute(Methods.Delete, regex, captureGroupNames)(callback)
  }

  /**
   * Adds a route to the action to process POST request with path.
   *
   * @param path
   * @param callback
   * @param pathPatternParser
   * @return
   */
  protected def post(path: String)
                    (callback: Request => Future[Response])
                    (implicit pathPatternParser: PathPatternParser) {
    addRoute(Methods.Post, path)(callback)
  }

  /**
   * Adds a route to the action to process POST request by [[Regex]]
   *
   * @param regex
   * @param captureGroupNames
   * @param callback
   * @return
   */
  protected def post(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                    (callback: Request => Future[Response]) {
    addRoute(Methods.Post, regex, captureGroupNames)(callback)
  }

  /**
   * Adds a route to the action to process PUT request with path.
   *
   * @param path
   * @param callback
   * @param pathPatternParser
   * @return
   */
  protected def put(path: String)
                   (callback: Request => Future[Response])
                   (implicit pathPatternParser: PathPatternParser) {
    addRoute(Methods.Put, path)(callback)
  }

  /**
   * Adds a route to the action to process PUT request by [[Regex]].
   *
   * @param regex
   * @param captureGroupNames
   * @param callback
   * @return
   */
  protected def put(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                   (callback: Request => Future[Response]) {
    addRoute(Methods.Put, regex, captureGroupNames)(callback)
  }

  /**
   * Adds a route to the action to process HEAD request with path.
   *
   * @param path
   * @param callback
   * @param pathPatternParser
   * @return
   */
  protected def head(path: String)
                    (callback: Request => Future[Response])
                    (implicit pathPatternParser: PathPatternParser) {
    addRoute(Methods.Head, path)(callback)
  }

  /**
   * Adds a route to the action to process HEAD request by [[Regex]].
   *
   * @param regex
   * @param captureGroupNames
   * @param callback
   * @return
   */
  protected def head(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                    (callback: Request => Future[Response]) {
    addRoute(Methods.Head, regex, captureGroupNames)(callback)
  }

  /**
   * Adds a route to the action to process PATCH request with path.
   *
   * @param path
   * @param callback
   * @param pathPatternParser
   * @return
   */
  protected def patch(path: String)
                     (callback: Request => Future[Response])
                     (implicit pathPatternParser: PathPatternParser) {
    addRoute(Methods.Post, path)(callback)
  }

  /**
   * Adds a route to the action to process PATCH request by [[Regex]].
   *
   * @param regex
   * @param captureGroupNames
   * @param callback
   * @return
   */
  protected def patch(regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                     (callback: Request => Future[Response]) {
    addRoute(Methods.Post, regex, captureGroupNames)(callback)
  }

  /**
   * Adds a route to the action with path.
   *
   * @param method
   * @param path
   * @param callback
   * @param pathPatternParser
   * @return
   */
  protected def addRoute(method: Methods.Value, path: String)
                        (callback: Request => Future[Response])
                        (implicit pathPatternParser: PathPatternParser) {
    val route = RouteDef(method, pathPatternParser(path), Action(callback))
    routeDefs.append(route)
  }

  /**
   * Adds a route to the action by [[Regex]].
   *
   * @param method
   * @param regex
   * @param captureGroupNames
   * @param callback
   * @return
   */
  protected def addRoute(method: Methods.Value, regex: Regex, captureGroupNames: Seq[String] = Seq.empty)
                        (callback: Request => Future[Response]) {
    val route = RouteDef(method, PathPattern(regex, captureGroupNames), Action(callback))
    routeDefs.append(route)
  }

  /**
   * Gets defined routes.
   *
   * @return
   */
  def getRouteDefs: Seq[RouteDef] = routeDefs.toSeq

}
