package org.sisioh.trinity.domain.mvc.server

import java.io.File
import java.net.InetSocketAddress
import org.sisioh.config.{ConfigurationMode, Configuration}
import org.sisioh.trinity.domain.mvc.Environment

/**
 * サーバ用設定ファイルを読み込むためのサービス。
 */
object ServerConfigLoader {

  def loadConfiguration(enviroment: Environment.Value, serverConfigEventListener: Option[ServerConfigEventListener] = None): Configuration = {
    val configuration = Configuration.loadByMode(
      new File("."),
      if (enviroment == Environment.Product)
        ConfigurationMode.Prod
      else
        ConfigurationMode.Dev
    )
    serverConfigEventListener.foreach {
      el =>
        el.onLoadedConfig(configuration)
    }
    configuration
  }

  def loadServerConfig(configuration: Configuration): ServerConfig = {
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
