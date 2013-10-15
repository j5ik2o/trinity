package org.sisioh.trinity.domain.io.infrastructure.transport.codec.http

import org.sisioh.trinity.domain.io.transport.codec.http.{Request, RequestProxy}

abstract class AbstractRequestProxy(underlying: Request)
  extends RequestProxy

