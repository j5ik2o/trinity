package org.sisioh.trinity.view.scalate

import org.sisioh.trinity.domain.config.Config
import org.fusesource.scalate.TemplateEngine

case class ScalateEngineContextImpl(implicit val config: Config)
  extends ScalateEngineContext {

  val engine = new TemplateEngine()
  engine.workingDirectory = config.templateWorkDir

}


