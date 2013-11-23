package org.sisioh.trinity.view.scalate

import org.sisioh.trinity.domain.mvc.Bootstrap

trait BootstrapWithScalateSupport extends Bootstrap {

  implicit lazy val scalateContext = ScalateEngineContext.fromConfiguration(environment)(configuration)

}
