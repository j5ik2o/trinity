package org.sisioh.trinity.view.thymeleaf

import org.sisioh.trinity.view.TemplateEngineContext
import org.thymeleaf.TemplateEngine
import org.sisioh.trinity.domain.config.Config
import org.thymeleaf.templateresolver.ITemplateResolver

trait ThymeleafEngineContext extends TemplateEngineContext {

  val engine: TemplateEngine

  protected val resolver: ITemplateResolver

  val templateMode: TemplateMode.Value
  val characterEncoding: String
  val cacheable: Boolean
  val cacheTTLMs: Option[Long]
  val suffix: String
}

object ThymeleafEngineContext {

  def apply
  (templateMode: TemplateMode.Value = TemplateMode.XHTML,
   characterEncoding: String = "utf-8",
   suffix: String = ".html",
   cacheable: Boolean = true,
   cacheTTLMs: Option[Long] = None)(implicit config: Config): ThymeleafEngineContext =
    ThymeleafEngineContextImpl(templateMode, characterEncoding, suffix, cacheable, cacheTTLMs)

}
