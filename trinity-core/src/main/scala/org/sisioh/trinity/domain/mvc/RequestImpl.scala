package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.infrastructure.transport.codec.http.AbstractRequestProxy
import org.sisioh.trinity.domain.io.transport.codec.http
import org.sisioh.trinity.domain.io.transport.codec.http.Method

class RequestImpl
(underlying: http.Request,
 val routeParams: Map[String, String])
  extends AbstractRequestProxy(underlying) with Request {

  val multiParams: Map[String, MultiPartItem] =
    if (method == Method.Post) {
      content.markReaderIndex()
      val m = MultiPartItem.fromRequest(this)
      content.resetReaderIndex()
      m
    } else Map.empty[String, MultiPartItem]

}
