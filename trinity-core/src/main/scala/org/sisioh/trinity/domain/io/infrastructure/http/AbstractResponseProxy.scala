package org.sisioh.trinity.domain.io.infrastructure.http

import org.sisioh.trinity.domain.io.http.{Response, ResponseProxy}

abstract class AbstractResponseProxy(val underlying: Response)
  extends ResponseProxy


