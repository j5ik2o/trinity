package org.sisioh.trinity.domain.io.http

import scala.language.implicitConversions
import org.jboss.netty.handler.codec.http.HttpVersion

object ProtocolVersion extends Enumeration {

  val Http10 = Value("HTTP/1.0")

  val Http11 = Value("HTTP/1.1")

  private[domain] implicit def toNetty(value: ProtocolVersion.Value) =
    HttpVersion.valueOf(value.toString)

  private[domain] implicit def toTrinity(value: HttpVersion) =
    ProtocolVersion.withName(value.toString)

}
