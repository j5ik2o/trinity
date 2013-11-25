package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Request => FinagleRequest}
import scala.language.implicitConversions

trait Request extends Message {

  val toUnderlyingAsFinagle: FinagleRequest

  def response: Response

  override def toString() =
    Seq(
      s"protocolVersion = $protocolVersion",
      s"method = $method",
      s"uri = $uri",
      s"headers = $headers",
      s"content = $content"
    ).mkString("Request(", ", ", ")")

  override def equals(obj: Any): Boolean = obj match {
    case that: Request =>
      super.equals(that) && method == that.method && uri == that.uri
    case _ => false
  }

  override def hashCode: Int =
    31 * (super.hashCode + method.## + uri.##)

  def method: Method.Value

  def withMethod(method: Method.Value): this.type

  def uri: String

  def withUri(uri: String): this.type

}

object Request {

  def apply(request: FinagleRequest): Request =
    new RequestImpl(request)

  def apply(method: Method.Value, uri: String,
            protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11): Request =
    new RequestImpl(method, uri, protocolVersion = protocolVersion)

}
