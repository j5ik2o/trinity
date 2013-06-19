package org.sisioh.trinity.view

import org.fusesource.scalate.TemplateEngine
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.http.BodyRenderer

/**
 * Scalateに対応した[[org.sisioh.trinity.domain.http.BodyRenderer]]
 *
 * @param config
 * @param path
 * @param context
 */
case class ScalateRenderer
(path: String, context: Map[String, Any] = Map.empty)(implicit config: Config)
  extends BodyRenderer {

  private val engine = new TemplateEngine()
  engine.workingDirectory = config.templateWorkDir

  def render: String =
    engine.layout(config.templatePath + path, context)

}

