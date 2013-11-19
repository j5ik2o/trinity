package org.sisioh.trinity.domain.mvc.server

import java.net.SocketAddress
import scala.concurrent.duration.Duration

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
 requestTimeout: Option[Int] = None)


