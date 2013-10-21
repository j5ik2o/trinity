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
      s"status = $status",
      s"content = $content"
    ).mkString("Response(", ", ", ")")

  override def equals(obj: Any): Boolean = obj match {
    case that: Response =>
      super.equals(that) && status == that.status
    case _ => false
  }

  override def hashCode: Int =
    31 * (super.hashCode + status.hashCode)

  def status: ResponseStatus.Value

  def withStatus(status: ResponseStatus.Value): this.type

}

object Response {

  def apply(underlying: FinagleResponse): Response =
    ResponseImpl(underlying)

  def apply(status: ResponseStatus.Value = ResponseStatus.Ok, version: Version.Value = Version.Http11): Response =
    new ResponseImpl(status, version = version)

}
