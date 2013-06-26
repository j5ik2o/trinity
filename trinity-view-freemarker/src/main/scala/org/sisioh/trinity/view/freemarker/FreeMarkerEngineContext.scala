package org.sisioh.trinity.view.freemarker

import org.sisioh.trinity.view.TemplateEngineContext
import freemarker.template.Configuration
import org.sisioh.trinity.domain.config.Config
import scala.language.existentials

trait   FreeMarkerEngineContext extends TemplateEngineContext {

  val configuration: Configuration

}

object FreeMarkerEngineContext {

  def apply(refClass: Class[_])(implicit config: Config): FreeMarkerEngineContext =
    FreeMarkerEngineContextImpl(refClass)

}
