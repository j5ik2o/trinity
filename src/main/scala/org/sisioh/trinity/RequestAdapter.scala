package org.sisioh.trinity

import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.{HttpMethod, Cookie, CookieDecoder}
import scala.collection.JavaConverters._
import org.sisioh.scala.toolbox.LoggingEx

/**
 * Adapts a FinagleRquest to a FinatraRequest
 */
object RequestAdapter extends LoggingEx {

  def apply(rawRequest: FinagleRequest): Request =
    new Request(rawRequest)

}
