package org.sisioh.trinity.view.thymeleaf

import org.sisioh.trinity.domain.config.{Environment, Config}
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.{FileTemplateResolver, ClassLoaderTemplateResolver}

case class ThymeleafEngineContextImpl(implicit val config: Config)
  extends ThymeleafEngineContext {

  val engine = new TemplateEngine()

  protected val resolver = if (config.environment == Environment.Product) {
    val classLoaderTemplateResolver = new ClassLoaderTemplateResolver
    classLoaderTemplateResolver.setPrefix(config.templatePath.drop(1))
    classLoaderTemplateResolver
  } else {
    val fileTemplateResolver = new FileTemplateResolver
    fileTemplateResolver.setPrefix(config.localDocumentRoot + config.templatePath)
    fileTemplateResolver
  }
  resolver.setTemplateMode("XHTML")
  resolver.setSuffix(".html")
  resolver.setCacheable(true)
  engine.setTemplateResolver(resolver)

}

