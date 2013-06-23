package org.sisioh.trinity.view

import org.sisioh.trinity.domain.config.Config

trait TemplateEngineContext {

  implicit val config: Config

}
