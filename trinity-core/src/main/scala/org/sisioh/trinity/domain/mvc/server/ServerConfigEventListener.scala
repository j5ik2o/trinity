package org.sisioh.trinity.domain.mvc.server

import org.sisioh.config.Configuration

trait ServerConfigEventListener {

  protected[server] def onLoadedConfig(configuration: Configuration): Unit

}
