package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Response => FinagleResponse}
import org.sisioh.trinity.domain.io.infrastructure.http.ResponseImpl
import scala.language.implicitConversions

trait Response extends Message {

  val toUnderlyingAsFinagle: FinagleResponse

  override def toString() =
    Seq(
      s"protocolVersion = $protocolVersion",
      s"headers = $headers",
      s"responseStatus = $responseStatus",
      s"content = $content"
    ).mkString("Response(", ", ", ")")

  override def equals(obj: Any): Boolean = obj match {
    case that: Response =>
      super.equals(that) && responseStatus == that.responseStatus
    case _ => false
  }

  override def hashCode: Int =
    31 * (super.hashCode + responseStatus.##)

  def responseStatus: ResponseStatus.Value

  def withResponseStatus(status: ResponseStatus.Value): this.type

}

object Response {

  def apply(underlying: FinagleResponse): Response =
    ResponseImpl(underlying)

  def apply(responseStatus: ResponseStatus.Value = ResponseStatus.Ok,
            protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11): Response =
    new ResponseImpl(responseStatus, protocolVersion = protocolVersion)

}
