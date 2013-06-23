package org.sisioh.trinity.view.scalate

import org.sisioh.trinity.view.TemplateEngineContext
import org.fusesource.scalate.TemplateEngine
import org.sisioh.trinity.domain.config.Config

trait ScalateEngineContext extends TemplateEngineContext {

  val engine = new TemplateEngine()
  engine.workingDirectory = config.templateWorkDir

 engine.resourceLoader
}

object ScalateEngineContext {

  def apply()(implicit config: Config): ScalateEngineContext =
    ScalateEngineContextImpl()

}
