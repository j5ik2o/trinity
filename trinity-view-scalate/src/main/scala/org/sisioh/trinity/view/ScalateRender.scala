package org.sisioh.trinity.view

import org.fusesource.scalate.TemplateEngine
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.domain.http.BodyRender

/**
 * Scalateに対応した[[org.sisioh.trinity.domain.http.BodyRender]]
 *
 * @param config
 * @param path
 * @param context
 */
case class ScalateRender(path: String, context: Map[String, Any] = Map.empty)(implicit config: Config) extends BodyRender {

  private val engine = new TemplateEngine()
  engine.workingDirectory = config.templateWorkDir

  def render: String =
    engine.layout(config.templatePath + path, context)

}

