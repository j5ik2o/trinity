package org.sisioh.trinity.domain.mvc.server

import java.net.SocketAddress
import scala.concurrent.duration.Duration

case class ServerConfig
(nameOpt: Option[String] = None,
 bindAddressOpt: Option[SocketAddress] = None,
 awaitDurationOpt: Option[Duration] = None,
 statsEnabled: Boolean = false,
 statsPort: Option[Int] = None,
 maxRequestSizeOpt: Option[Int] = None,
 maxResponseSizeOpt: Option[Int] = None,
 maxConcurrentRequestsOpt: Option[Int] = None,
 hostConnectionMaxIdleTimeOpt: Option[Duration] = None,
 hostConnectionMaxLifeTimeOpt: Option[Duration] = None,
 requestTimeoutOpt: Option[Int] = None)


