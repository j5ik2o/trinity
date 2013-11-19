package org.sisioh.trinity.domain.mvc.server

import java.io.File
import java.net.InetSocketAddress
import org.sisioh.config.{ConfigurationMode, Configuration}
import org.sisioh.trinity.domain.mvc.Environment

object ServerConfigLoader {

  def load(enviroment: Environment.Value): ServerConfig = {
    val configuration = Configuration.loadByMode(
      new File("."),
      if (enviroment == Environment.Product)
        ConfigurationMode.Prod
      else
        ConfigurationMode.Dev
    )
    val serverConfiguration = ServerConfig(
      name = configuration.getStringValue("name"),
      bindAddress = configuration.getStringValue("bindAddress").map {
        bindAddress =>
          val splits = bindAddress.split(":")
          val host = splits(0)
          val port = splits(1).toInt
          new InetSocketAddress(host, port)
      },
      statsEnabled = configuration.getBooleanValue("stats.Enabled").getOrElse(false),
      statsPort = configuration.getIntValue("stats.port")
    )
    serverConfiguration
  }

}
