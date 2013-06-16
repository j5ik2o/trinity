package org.sisioh.trinity.view

import org.sisioh.trinity.Config
import org.fusesource.scalate.TemplateEngine

case class ScalateView(config: Config, path: String, context: Map[String, Any] = Map.empty) extends View {

  private val engine = new TemplateEngine()
  engine.workingDirectory = config.templateWorkDir

  def render: String =
    engine.layout(config.templatePath + path, context)

}

