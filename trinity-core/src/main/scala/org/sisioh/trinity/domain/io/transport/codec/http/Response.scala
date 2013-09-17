package org.sisioh.trinity.domain.io.transport.codec.http

import scala.language.implicitConversions
import com.twitter.finagle.http.{Response => FinagleResponse}
import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse, HttpVersion, DefaultHttpResponse}
import org.sisioh.trinity.domain.io.infrastructure.transport.codec.http.ResponseImpl

trait Response extends Message {

  val status: ResponseStatus.Value

  def withStatus(status: ResponseStatus.Value): this.type

}

object Response {

  implicit def toFinagle(self: Response): FinagleResponse =
    FinagleResponse(toNetty(self))

  implicit def toNetty(self: Response): NettyResponse =
    self match {
      case ResponseImpl(underlying) => underlying
      case _ => throw new IllegalArgumentException()
    }

  implicit def toTrinity(underlying: NettyResponse): Response =
    ResponseImpl(underlying)

  def apply(version: HttpVersion, status: ResponseStatus.Value): Response =
    new ResponseImpl(version, status)

}
