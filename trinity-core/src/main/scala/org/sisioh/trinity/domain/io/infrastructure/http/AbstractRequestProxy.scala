package org.sisioh.trinity.domain.io.infrastructure.http

import com.twitter.finagle.http.{Request => FinagleRequest}
import org.sisioh.trinity.domain.io.http.{Request, RequestProxy}

abstract class AbstractRequestProxy(val underlying: Request)
  extends RequestProxy {

}

