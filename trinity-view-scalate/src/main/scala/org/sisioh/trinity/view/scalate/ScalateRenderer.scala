package org.sisioh.trinity.view.scalate

import org.sisioh.trinity.view.TemplateRenderer

/**
 * Scalateに対応した[[org.sisioh.trinity.domain.http.BodyRenderer]]
 *
 * @param path
 * @param context
 */
case class ScalateRenderer
(path: String, context: Map[String, Any] = Map.empty)
(implicit val engineContext: ScalateEngineContext)
  extends TemplateRenderer {

  def render: String =
    engineContext.engine.layout(engineContext.config.templatePath + path, context)

}






