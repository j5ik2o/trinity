package org.sisioh.trinity.view.thymeleaf

import org.sisioh.trinity.domain.config.Config

case class ThymeleafEngineContextImpl(implicit val config: Config)
  extends ThymeleafEngineContext

