package org.sisioh.trinity.domain.controller

import com.twitter.finagle.stats.StatsReceiver
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.routing.{Routes, PathPatternParser}
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.http.ResponseBuilder

trait Controller extends Routes {

  implicit protected val config: Config
  implicit protected val pathParser: PathPatternParser

  implicit val application: TrinityApplication

  protected lazy val statsReceiver: StatsReceiver = application.statsReceiver

  protected lazy val stats = statsReceiver.scope("Controller")

  protected def responseBuilder = new ResponseBuilder

}
