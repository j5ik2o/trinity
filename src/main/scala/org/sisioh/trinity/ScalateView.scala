package org.sisioh.trinity

import org.fusesource.scalate.TemplateEngine
import java.io.File

case class ScalateView(config: Config, path: String, context: Map[String, Any]) extends View {

  private val engine = new TemplateEngine()
  engine.workingDirectory = config.templateWorkDir

  def render: String =
    engine.layout(config.templatePath + path, context)

}

