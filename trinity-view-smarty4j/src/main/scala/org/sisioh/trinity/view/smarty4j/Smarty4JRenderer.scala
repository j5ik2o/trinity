package org.sisioh.trinity.view.smarty4j

import org.sisioh.trinity.view.TemplateRenderer
import org.lilystudio.smarty4j.Context
import scala.collection.JavaConverters._
import java.io.StringWriter
import org.seasar.util.io.ResourceUtil
import org.sisioh.trinity.domain.config.Environment

case class Smarty4JRenderer
(path: String, context: Map[String, AnyRef] = Map.empty)
(implicit val engineContext: Smarty4JEngineContext)
  extends TemplateRenderer {

  private val fullpath = if (engineContext.config.environment == Environment.Development)
    engineContext.config.localDocumentRoot + "/" + path
  else
    ResourceUtil.getResourceAsFile(path).toString

  private val template = engineContext.engine.getTemplate(fullpath)

  def render: String = {
    val templateContext = new Context()
    context.foreach {
      case (k, v: Map[_, _]) => templateContext.set(k, v.asJava)
      case (k, v: Iterable[_]) => templateContext.set(k, v.asJava)
      case (k, v) => templateContext.set(k, v)
    }
    val writer = new StringWriter
    template.merge(templateContext, writer)
    writer.toString
  }

}
