package org.sisioh.trinity.view.scalate

import java.io.File
import org.fusesource.scalate.TemplateEngine
import org.sisioh.config.Configuration
import org.sisioh.trinity.domain.mvc.Environment

case class ScalateEngineContext(environment: Environment.Value,
                                templateWorkDir: File,
                                localDocumentRoot: String,
                                templatePath: String) {

  private[scalate] val engine = new TemplateEngine()

}

object ScalateEngineContext {

  def fromConfiguration(environment: Environment.Value)(configuration: Configuration): ScalateEngineContext = {
    val workDir = configuration.getStringValue("view.scalate.workDir").getOrElse(System.getProperty("java.io.tmpdir"))
    val localDocumentRoot = configuration.getStringValue("view.scalate.localDocumentRoot").getOrElse("src/main/resources")
    val templatePath = configuration.getStringValue("view.scalate.templatePath").getOrElse("/")
    ScalateEngineContext(environment, new File(workDir), localDocumentRoot, templatePath)
  }

}

