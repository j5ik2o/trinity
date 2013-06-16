package org.sisioh.trinity.view

import org.fusesource.scalate.TemplateEngine
import org.sisioh.trinity.domain.Config

/**
 * Scalateに対応した[[org.sisioh.trinity.view.View]]
 *
 * @param config
 * @param path
 * @param context
 */
case class ScalateView(config: Config, path: String, context: Map[String, Any] = Map.empty) extends View {

  private val engine = new TemplateEngine()
  engine.workingDirectory = config.templateWorkDir

  def render: String =
    engine.layout(config.templatePath + path, context)

}

