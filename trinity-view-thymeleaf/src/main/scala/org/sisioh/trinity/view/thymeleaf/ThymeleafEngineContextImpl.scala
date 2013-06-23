package org.sisioh.trinity.view.thymeleaf

import org.sisioh.trinity.domain.config.{Environment, Config}
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.{FileTemplateResolver, ClassLoaderTemplateResolver}


case class ThymeleafEngineContextImpl
(templateMode: TemplateMode.Value = TemplateMode.XHTML,
 characterEncoding: String = "utf-8",
 suffix: String = ".html",
 cacheable: Boolean = true,
 cacheTTLMs: Option[Long] = None)
(implicit val config: Config)
  extends ThymeleafEngineContext {

  val engine = new TemplateEngine()

  protected val resolver = if (config.environment == Environment.Product) {
    val classLoaderTemplateResolver = new ClassLoaderTemplateResolver
    val prefix = if (config.templatePath.startsWith("/"))
      config.templatePath.drop(1)
    else
      config.templatePath
    classLoaderTemplateResolver.setPrefix(prefix)
    classLoaderTemplateResolver
  } else {
    val fileTemplateResolver = new FileTemplateResolver
    fileTemplateResolver.setPrefix(config.localDocumentRoot + config.templatePath)
    fileTemplateResolver
  }
  resolver.setTemplateMode(templateMode.toString)
  resolver.setSuffix(suffix)
  resolver.setCacheable(cacheable)
  resolver.setCacheTTLMs(cacheTTLMs.map(_.asInstanceOf[java.lang.Long]).getOrElse(null.asInstanceOf[java.lang.Long]))
  engine.setTemplateResolver(resolver)
}

