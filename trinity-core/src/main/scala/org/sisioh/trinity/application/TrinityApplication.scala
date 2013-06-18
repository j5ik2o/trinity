package org.sisioh.trinity.application

import org.sisioh.trinity.domain._
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.domain.RouteId

trait TrinityApplication extends Routes {

  val config: Config

}

object TrinityApplication {

  def apply(config: Config, globalSetting: Option[GlobalSetting] = None) =
    new TrinityApplicationImpl(config, globalSetting)

}