package org.sisioh.trinity.view.thymeleaf

import org.thymeleaf.context.Context
import java.util.Locale
import scala.collection.JavaConverters._
import org.sisioh.trinity.view.TemplateRenderer


case class ThymeleafRenderer
(path: String, context: Map[String, AnyRef] = Map.empty, locale: Locale = Locale.getDefault)
(implicit val engineContext: ThymeleafEngineContext)
  extends TemplateRenderer {

  def render: String = {
    val templateContext = new Context(locale, context.asJava)
    engineContext.engine.process(path, templateContext)
  }

}

