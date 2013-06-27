package org.sisioh.trinity.view.scalate

import org.sisioh.trinity.view.TemplateRenderer
import com.twitter.util.Future
import org.sisioh.trinity.domain.config.Environment

/**
 * Scalateに対応した[[org.sisioh.trinity.domain.http.BodyRenderer]]
 *
 * @param path テンプレートへのパス
 * @param context コンテキスト
 */
case class ScalateRenderer
(path: String, context: Map[String, AnyRef] = Map.empty)
(implicit val engineContext: ScalateEngineContext)
  extends TemplateRenderer {

  val rootPath = if (engineContext.config.environment == Environment.Development) {
      engineContext.config.localDocumentRoot + engineContext.config.templatePath
    } else {
      engineContext.config.templatePath
    }


  def render = Future {
    engineContext.engine.layout(rootPath + path, context)
  }

}






