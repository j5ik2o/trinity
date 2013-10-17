package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import com.twitter.finagle.http.{Request => FinagleRequest}
import org.sisioh.trinity.domain.io.transport.codec.http.{Request, RequestProxy}

abstract class AbstractRequestProxy(val underlying: Request)
  extends RequestProxy {

}

