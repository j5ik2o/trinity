package org.sisioh.trinity.view.scalate

import java.io.File
import org.sisioh.trinity.domain.mvc.Environment
import org.fusesource.scalate.TemplateEngine

case class ScalateContext(environment: Environment.Value,
                          templateWorkDir: File,
                          localDocumentRoot: String,
                          templatePath: String) {
  private[scalate] val engine = new TemplateEngine()
}

