package org.sisioh.trinity.view.freemarker

import org.sisioh.trinity.view.TemplateRenderer
import java.util
import scala.collection.JavaConverters._
import java.io.StringWriter

case class FreeMarkerRenderer
(path: String, context: Map[String, AnyRef] = Map.empty)
(implicit val engineContext: FreeMarkerEngineContext)
  extends TemplateRenderer {

  val template = engineContext.configuration.getTemplate(path)

  def render: String = {
    val templateContext = new util.HashMap[String, AnyRef]()
    context.foreach {
      case (k, v: Map[_, _]) => templateContext.put(k, v.asJava)
      case (k, v: Iterable[_]) => templateContext.put(k, v.asJava)
      case (k, v) => templateContext.put(k, v)
    }
    val writer = new StringWriter()
    template.process(templateContext, writer)
    writer.toString
  }

}
