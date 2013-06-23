package org.sisioh.trinity.view.smarty4j

import org.sisioh.trinity.domain.config.Config
import org.lilystudio.smarty4j.Engine

case class Smarty4JEngineContextImpl(implicit val config: Config)
  extends Smarty4JEngineContext {

  val engine: Engine = new Engine()

}
