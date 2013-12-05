/*
 * Copyright 2013 Sisioh Project and others. (http://sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.trinity.domain.mvc.server

import java.io.{FileNotFoundException, File}
import java.net.InetSocketAddress
import org.sisioh.config.{ConfigurationMode, Configuration}
import org.sisioh.trinity.domain.mvc.Environment
import scala.util.{Failure, Try}

object ServerConfigLoader extends ServerConfigLoader

/**
 * サーバ用設定ファイルを読み込むためのサービス。
 */
class ServerConfigLoader {

  def loadConfiguration(applicationId: String,
                        enviroment: Environment.Value,
                        serverConfigEventListener: Option[ServerConfigEventListener] = None): Try[Configuration] = Try {
    val configuration = Configuration.loadByMode(
      new File(applicationId),
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
  }.recoverWith {
    case ex =>
      Failure(new FileNotFoundException(s"Configuration file is not found. please set config path to -Dconfig.file or -Dconfig.resource, -Dconfig.url. (applicationId = $applicationId, environment = $enviroment)"))
  }

  private def getKeyName(keyName: String, prefix: Option[String] = None) =
    prefix.map(_ + keyName).getOrElse(keyName)

  protected def loadTlsConfig(configuration: Configuration, prefix: Option[String]): Option[TlsConfig] = {
    configuration.getConfiguration(getKeyName("tls", prefix)).map {
      c =>
        val certificatePath = c.getStringValue("certificatePath").get
        val keyPath = c.getStringValue("keyPath").get
        val caCertificatePath = c.getStringValue("caCertificatePath")
        val ciphers = c.getStringValue("ciphers")
        val nextProtos = c.getStringValue("nextProtos")
        TlsConfig(certificatePath, keyPath, caCertificatePath, ciphers, nextProtos)
    }
  }

  def loadServerConfig(configuration: Configuration, prefix: Option[String] = None): ServerConfig = {
    val serverConfiguration = ServerConfig(
      name = configuration.
        getStringValue(getKeyName("name", prefix)),
      bindAddress = configuration.
        getStringValue(getKeyName("bindAddress", prefix)).map {
        bindAddress =>
          val splits = bindAddress.split(":")
          if (splits.size > 0) {
            val host = splits(0)
            val port = splits(1).toInt
            new InetSocketAddress(host, port)
          } else {
            new InetSocketAddress(bindAddress.toInt)
          }
      },
      statsEnabled = configuration.getBooleanValue(getKeyName("stats.Enabled", prefix)).getOrElse(false),
      statsPort = configuration.getIntValue(getKeyName("stats.port", prefix)),
      tlsConfig = loadTlsConfig(configuration, prefix)
    )
    serverConfiguration
  }

}
