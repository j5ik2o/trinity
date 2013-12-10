package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Request => FinagleRequest}
import java.net.{InetAddress, InetSocketAddress}
import scala.language.implicitConversions
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer

trait Request extends Message {

  val toUnderlyingAsFinagle: FinagleRequest

  def response: Response

  override def toString =
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

  def queryString = uri.split('?').last

  def method: Methods.Value

  def withMethod(method: Methods.Value): this.type

  def uri: String

  def withUri(uri: String): this.type

  def fileExtension = toUnderlyingAsFinagle.fileExtension

  def path: String = toUnderlyingAsFinagle.path

  def remoteHost = toUnderlyingAsFinagle.remoteHost

  def remoteSocketAddress: InetSocketAddress = toUnderlyingAsFinagle.remoteSocketAddress

  def remoteAddress: InetAddress = remoteSocketAddress.getAddress

  def remotePort: Int = remoteSocketAddress.getPort

  protected def _params = toUnderlyingAsFinagle.params

  def params: Map[String, String] = _params

  def getParamAsStringOpt(name: String): Option[String] = params.get(name)

  def getParamAsString(name: String, default: String): String = getParamAsStringOpt(name).getOrElse(default)

  def getParamAsShortOpt(name: String): Option[Short] = _params.getShort(name)

  def getParamAsShort(name: String, default: Short): Short = _params.getShortOrElse(name, default)

  def getParamAsIntOpt(name: String): Option[Int] = _params.getInt(name)

  def getParamAsInt(name: String, default: Int): Int = _params.getIntOrElse(name, default)

  def getParamAsLongOpt(name: String): Option[Long] = _params.getLong(name)

  def getParamAsLong(name: String, default: Long): Long = _params.getLongOrElse(name, default)

  def getParamAsBooleanOpt(name: String): Option[Boolean] = _params.getBoolean(name)

  def getParamAsBoolean(name: String, default: Boolean): Boolean = _params.getBooleanOrElse(name, default)

  def getParamAsSeq(name: String): Seq[String] = _params.getAll(name).toList

  def getParams: Seq[(String, String)] = params.toList.map {
    case (k, v) =>
      (k, v)
  }

  def containsParam(name: String): Boolean = params.contains(name)

  def getParamNames: Set[String] = params.keySet

}

object Request {

  private[trinity] def apply(request: FinagleRequest): Request =
    new RequestImpl(request)

  def apply(method: Methods.Value,
            uri: String,
            headers: Seq[(HeaderName, Any)] = Seq.empty,
            cookies: Seq[Cookie] = Seq.empty,
            attributes: Map[String, Any] = Map.empty,
            content: ChannelBuffer = ChannelBuffer.empty,
            isMutable: Boolean = false,
            protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11): Request =
    new RequestImpl(method, uri, headers, cookies, attributes, content, isMutable, protocolVersion)

}
