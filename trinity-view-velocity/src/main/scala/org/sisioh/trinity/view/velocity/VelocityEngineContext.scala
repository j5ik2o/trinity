package org.sisioh.trinity.view.velocity

import org.sisioh.trinity.view.TemplateEngineContext
import org.apache.velocity.app.VelocityEngine
import org.sisioh.trinity.domain.config.Config

trait VelocityEngineContext extends TemplateEngineContext {

  val engine: VelocityEngine

}

object VelocityEngineContext {

  def apply()(implicit config: Config): VelocityEngineContext =
    new VelocityEngineContextImpl()

}

