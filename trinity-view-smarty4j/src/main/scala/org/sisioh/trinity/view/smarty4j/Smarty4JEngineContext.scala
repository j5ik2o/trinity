package org.sisioh.trinity.view.smarty4j

import org.lilystudio.smarty4j.Engine
import org.sisioh.trinity.view.TemplateEngineContext
import org.sisioh.trinity.domain.config.Config

trait Smarty4JEngineContext extends TemplateEngineContext {

  val engine: Engine

}

object Smarty4JEngineContext {

  def apply()(implicit config: Config): Smarty4JEngineContext =
    Smarty4JEngineContextImpl()

}
