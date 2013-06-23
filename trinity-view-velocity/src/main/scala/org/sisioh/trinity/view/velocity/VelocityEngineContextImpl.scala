package org.sisioh.trinity.view.velocity

import org.apache.velocity.app.VelocityEngine
import org.sisioh.trinity.domain.config.Config
import java.util.Properties
import org.apache.velocity.runtime.RuntimeConstants

case class VelocityEngineContextImpl
(implicit val config: Config)
  extends VelocityEngineContext {

  val engine = new VelocityEngine()
  private val properties = new Properties()
  properties.setProperty(RuntimeConstants.RESOURCE_LOADER, "class")
  properties.setProperty("class.resource.loader.class",
    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
  engine.init(properties)

}
