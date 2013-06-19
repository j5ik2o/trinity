package org.sisioh.trinity.application

import com.twitter.finagle.SimpleFilter
import com.twitter.finagle.http.{Response, Request}
import com.twitter.finagle.stats.StatsReceiver
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.controller.{Controller, GlobalSetting}
import org.sisioh.trinity.domain.routing.Routes

trait TrinityApplication extends Routes {

  val config: Config

  val statsReceiver: StatsReceiver

  def registerController(controller: Controller): Unit

  def registerFilter(filter: SimpleFilter[Request, Response])


  def start(): Unit

  def shutdown(): Unit

}

object TrinityApplication {

  private var currentApplication: TrinityApplication = _

  def apply(config: Config, globalSetting: Option[GlobalSetting] = None): TrinityApplication = synchronized {
    if (currentApplication == null) {
      currentApplication = new TrinityApplicationImpl(config, globalSetting)
      currentApplication
    } else {
      currentApplication
    }
  }

  implicit def current = currentApplication

}