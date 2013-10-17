package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import org.sisioh.trinity.domain.io.transport.codec.http.{Response, ResponseProxy}

abstract class AbstractResponseProxy(val underlying: Response)
  extends ResponseProxy


