package org.sisioh.trinity.domain.mvc

import scala.concurrent.duration.Duration
import java.net.SocketAddress

case class ServerConfig
(nameOpt: Option[String] = None,
 bindAddressOpt: Option[SocketAddress] = None,
 awaitDurationOpt: Option[Duration] = None,
 maxRequestSizeOpt: Option[Int] = None,
 maxResponseSizeOpt: Option[Int] = None,
 maxConcurrentRequestsOpt: Option[Int] = None,
 hostConnectionMaxIdleTimeOpt: Option[Duration] = None,
 hostConnectionMaxLifeTimeOpt: Option[Duration] = None,
 requestTimeoutOpt: Option[Int] = None)


