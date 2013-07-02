package org.sisioh.trinity.view.velocity

import org.sisioh.trinity.view.TemplateRenderer
import org.apache.velocity.VelocityContext
import scala.collection.JavaConverters._
import java.io.StringWriter
import com.twitter.util.Future

case class VelocityRenderer
(path: String, context: Map[String, AnyRef] = Map.empty)
(implicit val engineContext: VelocityEngineContext)
  extends TemplateRenderer {
  val template = engineContext.engine.getTemplate(path)

  def render = Future {
    val templateContext = new VelocityContext()
    context.foreach {
      case (k, v: Map[_, _]) => templateContext.put(k, v.asJava)
      case (k, v: Iterable[_]) => templateContext.put(k, v.asJava)
      case (k, v) => templateContext.put(k, v)
    }
    val writer = new StringWriter()
    template.merge(templateContext, writer)
    writer.toString
  }
}
