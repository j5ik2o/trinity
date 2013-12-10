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

case class TlsConfig
(certificatePath: String,
 keyPath: String,
 caCertificatePath: Option[String],
 ciphers: Option[String],
 nextProtos: Option[String])

case class Engine(self: SSLEngine,
                  handlesRenegotiation: Boolean = false,
                  certId: String = "<unknown>")

/**
 * [[org.sisioh.trinity.domain.mvc.server.Server]]のための設定。
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
 statsEnabled: Boolean = false,
 statsPort: Option[Int] = None,
 maxRequestSize: Option[Int] = None,
 maxResponseSize: Option[Int] = None,
 maxConcurrentRequests: Option[Int] = None,
 hostConnectionMaxIdleTime: Option[Duration] = None,
 hostConnectionMaxLifeTime: Option[Duration] = None,
 requestTimeout: Option[Int] = None,
 newSSLEngine: Option[() => Engine] = None,
 tlsConfig: Option[TlsConfig] = None)


