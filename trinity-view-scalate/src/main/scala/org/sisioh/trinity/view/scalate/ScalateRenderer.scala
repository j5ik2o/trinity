package org.sisioh.trinity.view.scalate

import org.sisioh.trinity.view.TemplateRenderer
import com.twitter.util.Future

/**
 * Scalateに対応した[[org.sisioh.trinity.domain.http.BodyRenderer]]
 *
 * @param path
 * @param context
 */
case class ScalateRenderer
(path: String, context: Map[String, AnyRef] = Map.empty)
(implicit val engineContext: ScalateEngineContext)
  extends TemplateRenderer {

  def render = Future {
    engineContext.engine.layout(engineContext.config.templatePath + path, context)
  }

}






