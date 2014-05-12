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

import java.util.concurrent.atomic.AtomicLong
import org.sisioh.trinity.domain.io.http.Methods
import org.sisioh.trinity.domain.mvc.action.Action
import org.sisioh.trinity.domain.mvc.routing.pathpattern.{PathPatternParser, PathPattern}
import scala.concurrent.Future

/**
 * Represents the route to the action.
 */
private[mvc]
trait Route[Req, Rep]
  extends Serializable with Ordered[Route[Req, Rep]] {

  val identifier: RouteId

  override def equals(obj: Any): Boolean = obj match {
    case that: Route[_, _] => this.identifier == that.identifier
    case _ => false
  }

  override def hashCode = 31 * identifier.##

  def compare(that: Route[Req, Rep]): Int = {
    this.order compareTo that.order
  }

  private val order = Route.orderGenerator.getAndIncrement

  /**
   * The action this route has.
   */
  val action: Action[Req, Rep]

  /**
   * Applies the request to the action this route has.
   *
   * @param request リクエスト
   * @return `Future`
   */
  def apply(request: Req): Future[Rep] = action(request)

}

/**
 * Represents the companion object for [[Route]].
 */
object Route {

  /**
   * Creates the instance as [[Route]].
   *
   * @param identity [[org.sisioh.trinity.domain.mvc.routing.RouteId]]
   * @param action [[org.sisioh.trinity.domain.mvc.action.Action]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](identity: RouteId, action: Action[Req, Rep]): Route[Req, Rep] =
    new RouteImpl(identity, action)

  /**
   * ファクトリメソッド。
   *
   * @param method メソッド
   * @param pathPattern [[org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPattern]]
   * @param action [[org.sisioh.trinity.domain.mvc.action.Action]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](method: Methods.Value, pathPattern: PathPattern, action: Action[Req, Rep]): Route[Req, Rep] =
    new RouteImpl(RouteId(method, pathPattern), action)

  /**
   * ファクトリメソッド。
   *
   * @param method メソッド
   * @param path パス
   * @param action [[org.sisioh.trinity.domain.mvc.action.Action]]
   * @param pathPatternParser [[org.sisioh.trinity.domain.mvc.routing.pathpattern.PathPatternParser]]
   * @return [[org.sisioh.trinity.domain.mvc.routing.Route]]
   */
  def apply[Req, Rep](method: Methods.Value, path: String, action: Action[Req, Rep])
                     (implicit pathPatternParser: PathPatternParser): Route[Req, Rep] = {
    val pathPattern = pathPatternParser(path)
    new RouteImpl(RouteId(method, pathPattern), action)
  }

  /**
   * エクストラクタメソッド。
   *
   * @param route [[org.sisioh.trinity.domain.mvc.routing.Route]]
   * @return 構成要素
   */
  def unapply[Req, Rep](route: Route[Req, Rep]): Option[(RouteId, Action[Req, Rep])] =
    Some(route.identifier, route.action)

  private val orderGenerator = new AtomicLong()

}

