package org.sisioh.trinity.test

import com.twitter.finagle.http.{Response => FinagleResponse}
import org.jboss.netty.util.CharsetUtil._

class MockResponse(val originalResponse: FinagleResponse) {

  def status = originalResponse.getStatus()

  def code = originalResponse.getStatus().getCode

  def body = originalResponse.getContent().toString(UTF_8)

  def getHeader(name: String) = originalResponse.getHeader(name)

  def getHeaders = originalResponse.getHeaders()

}
