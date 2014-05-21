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

import java.net.SocketAddress
import javax.net.ssl.SSLEngine
import scala.concurrent.duration.Duration

/**
 * Represents the configuration item for finagle's OpenConnectionsThresholds.
 *
 * @param lowWaterMark
 * @param highWaterMark
 * @param idleTime
 */
case class OpenConnectionsThresholdsConfig
(lowWaterMark: Int,
 highWaterMark: Int,
 idleTime: Duration)

/**
 * Represents the configuration item for TLS.
 *
 * @param certificatePath
 * @param keyPath
 * @param caCertificatePath
 * @param ciphers
 * @param nextProtos
 */
case class TlsConfig
(certificatePath: String,
 keyPath: String,
 caCertificatePath: Option[String],
 ciphers: Option[String],
 nextProtos: Option[String])

/**
 * Represents the configuration item for SSL engine.
 *
 * @param self [[SSLEngine]]
 * @param handlesRenegotiation
 * @param certId
 */
case class Engine(self: SSLEngine,
                  handlesRenegotiation: Boolean = false,
                  certId: String = "<unknown>")

/**
 * Represents the configuration item for [[Server]].
 *
 * @param name
 * @param bindAddress
 * @param awaitDuration
 * @param statsEnabled
 * @param statsPort
 * @param maxRequestSize
 * @param maxResponseSize
 * @param maxConcurrentRequests
 * @param hostConnectionMaxIdleTime
 * @param hostConnectionMaxLifeTime
 * @param requestTimeout
 * @param newSSLEngine
 * @param tlsConfig
 */
case class ServerConfig
(name: Option[String] = None,
 bindAddress: Option[SocketAddress] = None,
 awaitDuration: Option[Duration] = None,
 finagleLogging: Boolean = true,
 statsEnabled: Boolean = false,
 statsPort: Option[Int] = None,
 keepAlive: Option[Boolean] = None,
 maxRequestSize: Option[Int] = None,
 maxResponseSize: Option[Int] = None,
 maxConcurrentRequests: Option[Int] = None,
 hostConnectionMaxIdleTime: Option[Duration] = None,
 hostConnectionMaxLifeTime: Option[Duration] = None,
 requestTimeout: Option[Duration] = None,
 readTimeout: Option[Duration] = None,
 writeCompletionTimeout: Option[Duration] = None,
 sendBufferSize: Option[Int] = None,
 receiveBufferSize: Option[Int] = None,
 openConnectionsThresholdsConfig: Option[OpenConnectionsThresholdsConfig] = None,
 newSSLEngine: Option[() => Engine] = None,
 tlsConfig: Option[TlsConfig] = None)


