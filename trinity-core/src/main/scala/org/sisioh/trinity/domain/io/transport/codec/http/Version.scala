package org.sisioh.trinity.domain.io.transport.codec.http

import org.jboss.netty.handler.codec.http.HttpVersion

object Version extends Enumeration {

  val Http10 = Value("HTTP/1.0")

  val Http11 = Value("HTTP/1.1")

  private[domain] implicit def toNetty(value: Version.Value) =
    HttpVersion.valueOf(value.toString)

  private[domain] implicit def toTrinity(value: HttpVersion) =
    Version.withName(value.toString)

}
