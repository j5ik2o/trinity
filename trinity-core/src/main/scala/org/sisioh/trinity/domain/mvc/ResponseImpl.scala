package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.infrastructure.transport.codec.http.AbstractResponseProxy
import org.sisioh.trinity.domain.io.transport.codec.http

class ResponseImpl(underlying: http.Response)
  extends AbstractResponseProxy(underlying) with Response {

}
