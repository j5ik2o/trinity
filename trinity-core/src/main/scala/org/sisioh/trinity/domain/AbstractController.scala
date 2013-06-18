package org.sisioh.trinity.domain

import org.sisioh.trinity.application.TrinityApplication
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}

abstract class AbstractController(application: TrinityApplication, protected val statsReceiver: StatsReceiver = NullStatsReceiver) extends Controller {

  implicit protected val config: Config = application.config

  implicit protected val pathParser: PathPatternParser = new SinatraPathPatternParser()

  val routeRepository: RouteRepositoryOnMemory = new RouteRepositoryOnMemory

}
