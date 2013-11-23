package org.sisioh.trinity.view.scalate

import org.sisioh.trinity.domain.mvc.http.ResponseBuilder

object Implicits {

  implicit class ResponseBuilderEx(val responseBuilder: ResponseBuilder) extends AnyVal {

    def withScalate(path: String, context: Map[String, AnyRef] = Map.empty)
                   (implicit scalateContext: ScalateEngineContext): ResponseBuilder =
      responseBuilder.withRenderer(ScalateRenderer(path, context))

  }

}

