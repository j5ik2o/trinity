/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
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
package org.sisioh.trinity.domain.controller

import com.twitter.finagle.stats.StatsReceiver
import java.util.UUID
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity, Entity}
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.http.{TrinityResponseImplicitSupport, TrinityResponseBuilder}
import org.sisioh.trinity.domain.routing.{Routes, PathPatternParser}
import scala.language.implicitConversions

/**
 * アクションを集合で保持するコントローラ。
 */
trait Controller
  extends Entity[Identity[UUID]]
  with Routes
  with EntityCloneable[Identity[UUID], Controller]
  with Ordered[Controller]
  with TrinityResponseImplicitSupport {

  def compare(that: Controller): Int =
    identity.value.compareTo(that.identity.value)

  val identity: Identity[UUID] = Identity(UUID.randomUUID())

  implicit protected val config: Config

  implicit protected val pathParser: PathPatternParser

  implicit val application: TrinityApplication

  protected lazy val statsReceiver: StatsReceiver = application.statsReceiver

  protected lazy val stats = statsReceiver.scope("Controller")

  protected def responseBuilder = TrinityResponseBuilder()


}
