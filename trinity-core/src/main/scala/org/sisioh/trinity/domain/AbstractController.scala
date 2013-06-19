package org.sisioh.trinity.domain

import org.sisioh.trinity.application.TrinityApplication
import com.twitter.finagle.stats.StatsReceiver

abstract class AbstractController()(implicit val application: TrinityApplication) extends Controller {

  implicit protected val config: Config = application.config

  implicit protected val pathParser: PathPatternParser = new SinatraPathPatternParser()

  protected lazy val statsReceiver: StatsReceiver = application.statsReceiver

  val routeRepository: RouteRepositoryOnMemory = new RouteRepositoryOnMemory

}
