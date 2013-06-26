package org.sisioh.trinity.domain.controller

import com.twitter.finagle.stats.StatsReceiver
import java.util.UUID
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity, Entity}
import org.sisioh.trinity.application.TrinityApplication
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.http.{Response, ResponseBuilder}
import org.sisioh.trinity.domain.routing.{Routes, PathPatternParser}
import scala.language.implicitConversions

trait Controller
  extends Entity[Identity[UUID]] with Routes
  with EntityCloneable[Identity[UUID], Controller] {

  val identity: Identity[UUID] = Identity(UUID.randomUUID())

  implicit protected val config: Config
  implicit protected val pathParser: PathPatternParser

  implicit val application: TrinityApplication

  protected lazy val statsReceiver: StatsReceiver = application.statsReceiver

  protected lazy val stats = statsReceiver.scope("Controller")

  protected def responseBuilder = ResponseBuilder()

  implicit def convertToRaw(res : Response) =
    res.get

}
