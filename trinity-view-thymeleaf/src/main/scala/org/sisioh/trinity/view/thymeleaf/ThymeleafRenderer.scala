package org.sisioh.trinity.view.thymeleaf

import org.thymeleaf.context.Context
import java.util.Locale
import org.sisioh.trinity.view.TemplateRenderer
import scala.collection.JavaConverters._
import com.twitter.util.Future

case class ThymeleafRenderer
(path: String, context: Map[String, AnyRef] = Map.empty, locale: Locale = Locale.getDefault)
(implicit val engineContext: ThymeleafEngineContext)
  extends TemplateRenderer {

  def render = Future {
    val templateContext = new Context(locale)
    context.foreach {
      case (k, v: Map[_, _]) => templateContext.setVariable(k, v.asJava)
      case (k, v: Iterable[_]) => templateContext.setVariable(k, v.asJava)
      case (k, v) => templateContext.setVariable(k, v)
    }
    engineContext.engine.process(path, templateContext)
  }

}

