package org.sisioh.trinity.view

import org.sisioh.trinity.domain.http.BodyRenderer

trait TemplateRenderer extends BodyRenderer {

  implicit val engineContext: TemplateEngineContext

}
