package org.sisioh.trinity

import org.fusesource.scalate.TemplateEngine
import java.io.File

case class ScalateView(path: String, context: Map[String, Any]) extends View {

  private val engine = new TemplateEngine()
  engine.workingDirectory = new File("./tmp")

  def render: String =
    engine.layout(Config.get("template_path") + path, context)

}
