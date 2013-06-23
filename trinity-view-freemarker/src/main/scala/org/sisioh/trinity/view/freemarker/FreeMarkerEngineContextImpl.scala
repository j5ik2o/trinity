package org.sisioh.trinity.view.freemarker

import org.sisioh.trinity.domain.config.Config
import freemarker.template.Configuration

case class FreeMarkerEngineContextImpl(refClass: Class[_])(implicit val config: Config)
  extends FreeMarkerEngineContext {

  val configuration = new Configuration()
  configuration.setClassForTemplateLoading(refClass, "")

}
