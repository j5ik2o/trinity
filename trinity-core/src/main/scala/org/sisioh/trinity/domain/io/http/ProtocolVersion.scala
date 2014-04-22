package org.sisioh.trinity.domain.io.http

import org.jboss.netty.handler.codec.http.HttpVersion
import scala.language.implicitConversions

/**
 * Represents the enumeration for HTTP protocol versions.
 */
object ProtocolVersion extends Enumeration {

  /**
   * HTTP ver 1.0
   */
  val Http10 = Value("HTTP/1.0")

  /**
   * HTTP ver 1.1
   */
  val Http11 = Value("HTTP/1.1")

  private[domain] implicit def toNetty(value: ProtocolVersion.Value) =
    HttpVersion.valueOf(value.toString)

  private[domain] implicit def toTrinity(value: HttpVersion) =
    ProtocolVersion.withName(value.toString)

}
