package org.sisioh.trinity.view.thymeleaf

import org.sisioh.trinity.view.TemplateEngineContext
import org.thymeleaf.TemplateEngine
import org.sisioh.trinity.domain.config.Config
import org.thymeleaf.templateresolver.ITemplateResolver

trait ThymeleafEngineContext extends TemplateEngineContext {

  val engine: TemplateEngine

  protected val resolver : ITemplateResolver

}

object ThymeleafEngineContext {

  def apply()(implicit config: Config): ThymeleafEngineContext =
    ThymeleafEngineContextImpl()

}
