package org.sisioh.trinity.domain.mvc.server

import java.io.File
import java.net.InetSocketAddress
import org.sisioh.config.Configuration

object ServerConfigLoader {

  def load: ServerConfig = {
    val configuration = Configuration.loadByMode(new File("."))
    val serverConfiguration = ServerConfig(
      nameOpt = configuration.getStringValue("name"),
      bindAddressOpt = configuration.getStringValue("bindAddress").map {
        bindAddress =>
          val splits = bindAddress.split(":")
          val host = splits(0)
          val port = splits(1).toInt
          new InetSocketAddress(host, port)
      }
    )
    serverConfiguration
  }

}
