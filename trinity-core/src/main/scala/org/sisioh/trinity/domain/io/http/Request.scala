package org.sisioh.trinity.domain.io.http

import scala.language.implicitConversions
import org.sisioh.trinity.domain.io.infrastructure.http.RequestImpl
import com.twitter.finagle.http.{Request => FinagleRequest}

trait Request extends Message {

  val toUnderlyingAsFinagle: FinagleRequest

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
      method == that.method && uri == that.uri
    case _ => false
  }

  override def hashCode: Int =
    31 * (method.## + uri.##)

  val method: Method.Value

  def withMethod(method: Method.Value): this.type

  val uri: String

  def withUri(uri: String): this.type

}

object Request {

  def apply(method: Method.Value, uri: String, version: Version.Value = Version.Http11): Request =
    new RequestImpl(method, uri, version = version)

  def apply(request: FinagleRequest): Request =
    new RequestImpl(request)
}
