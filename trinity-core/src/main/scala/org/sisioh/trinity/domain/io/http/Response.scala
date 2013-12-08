package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Response => FinagleResponse}
import scala.language.implicitConversions
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

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

  private[trinity] def apply(underlying: FinagleResponse): Response =
    ResponseImpl(underlying)

  def apply(responseStatus: ResponseStatus.Value = ResponseStatus.Ok,
            headers: Seq[(HeaderName, Any)] = Seq.empty,
            cookies: Seq[Cookie] = Seq.empty,
            attributes: Map[String, Any] = Map.empty[String, Any],
            content: ChannelBuffer = ChannelBuffer.empty,
            isMutable: Boolean = false,
            protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11): Response =
    new ResponseImpl(responseStatus, headers, cookies, attributes, content, isMutable, protocolVersion)

}
