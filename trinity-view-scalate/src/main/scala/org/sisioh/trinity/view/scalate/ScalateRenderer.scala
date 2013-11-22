package org.sisioh.trinity.view.scalate

import org.sisioh.trinity.domain.io.http.ContentType
import org.sisioh.trinity.domain.mvc.Environment
import org.sisioh.trinity.domain.mvc.http.{ResponseBuilder, ResponseRenderer}

case class ScalateRenderer(path: String, context: Map[String, AnyRef] = Map.empty)
                          (implicit scalateContext: ScalateContext) extends ResponseRenderer {

  private val engine = scalateContext.engine

  engine.workingDirectory = scalateContext.templateWorkDir

  val rootPath = if (scalateContext.environment == Environment.Development) {
    scalateContext.localDocumentRoot + scalateContext.templatePath
  } else {
    scalateContext.templatePath
  }

  def render(responseBuilder: ResponseBuilder): Unit = {
    responseBuilder.
      withContent(engine.layout(rootPath + path, context)).
      withContentType(ContentType.TextHtml)
  }

}
