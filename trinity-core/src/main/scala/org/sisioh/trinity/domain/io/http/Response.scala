package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Response => FinagleResponse}
import scala.language.implicitConversions
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

/**
 * Represents the trait for HTTP response.
 */
trait Response extends Message {

  override val toUnderlyingAsFinagle: FinagleResponse

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

  /**
   * Gets the response status.
   *
   * @return response status
   */
  def responseStatus: ResponseStatus.Value

  /**
   * Creates a new instance with a new response status.
   *
   * @param status response status
   * @return a new instance
   */
  def withResponseStatus(status: ResponseStatus.Value): this.type

}

/**
 * Represents the companion object for [[Response]].
 */
object Response {

  private[trinity] def apply(underlying: FinagleResponse): Response =
    ResponseImpl(underlying)

  /**
   * Creates a [[Response]]'s instance.
   *
   * @param responseStatus response status
   * @param headers headers
   * @param cookies cookies
   * @param attributes attributes
   * @param content content
   * @param isMutable true if it's mutable
   * @param protocolVersion protocol version
   * @return [[Response]]
   */
  def apply(responseStatus: ResponseStatus.Value = ResponseStatus.Ok,
            headers: Seq[(HeaderName, Any)] = Seq.empty,
            cookies: Seq[Cookie] = Seq.empty,
            attributes: Map[String, Any] = Map.empty[String, Any],
            content: ChannelBuffer = ChannelBuffer.empty,
            isMutable: Boolean = false,
            protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11): Response =
    new ResponseImpl(responseStatus, headers, cookies, attributes, content, isMutable, protocolVersion)

}
