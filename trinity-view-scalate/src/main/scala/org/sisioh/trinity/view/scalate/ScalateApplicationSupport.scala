package org.sisioh.trinity.view.scalate

import org.sisioh.trinity._

trait ScalateApplicationSupport {
  this: Application =>

  implicit lazy val scalateContext = ScalateEngineContext.fromConfiguration(environment)(configuration)

}
