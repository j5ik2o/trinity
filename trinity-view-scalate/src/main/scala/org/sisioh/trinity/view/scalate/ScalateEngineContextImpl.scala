package org.sisioh.trinity.view.scalate

import org.sisioh.trinity.domain.config.Config

case class ScalateEngineContextImpl(implicit val config: Config)
  extends ScalateEngineContext


