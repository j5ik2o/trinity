package org.sisioh.trinity.domain

import com.twitter.finagle.stats.StatsReceiver

trait Controller extends Routes {

  implicit protected val config: Config
  implicit protected val pathParser: PathPatternParser

  protected val statsReceiver: StatsReceiver

  protected lazy val stats = statsReceiver.scope("Controller")

  protected def responseBuilder = new ResponseBuilder
}
