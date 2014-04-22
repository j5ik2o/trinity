package org.sisioh.trinity.domain.io.http

import com.twitter.finagle.http.{Request => FinagleRequest, ParamMap}
import java.net.{InetAddress, InetSocketAddress}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import scala.language.implicitConversions

/**
 * Represents the trait for HTTP request.
 */
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

  /**
   * Gets the query string this request has.
   *
   * @return query string
   */
  def queryString = uri.split('?').last

  /**
   * Gets the method this request has.
   *
   * @return method
   */
  def method: Methods.Value

  /**
   * Creates a new instance with a new method.
   *
   * @param method
   * @return a new instance
   */
  def withMethod(method: Methods.Value): this.type

  /**
   * Gets the URI this request has.
   *
   * @return URI
   */
  def uri: String

  /**
   * Creates a new instance with a new URI.
   *
   * @param uri
   * @return a new instance
   */
  def withUri(uri: String): this.type

  /**
   * Gets the file extension contained in the path.
   *
   * @return file extension
   */
  def fileExtension = toUnderlyingAsFinagle.fileExtension

  /**
   * Gets the path this request has.
   *
   * @return path
   */
  def path: String = toUnderlyingAsFinagle.path

  /**
   * Gets the remote host this request has.
   *
   * @return remote host
   */
  def remoteHost = toUnderlyingAsFinagle.remoteHost

  /**
   * Gets the remote socket address this request has.
   *
   * @return remote socket address
   */
  def remoteSocketAddress: InetSocketAddress = toUnderlyingAsFinagle.remoteSocketAddress

  /**
   * Gets the remote address this request has.
   *
   * @return remote address
   */
  def remoteAddress: InetAddress = remoteSocketAddress.getAddress

  /**
   * Gets the remote port this request has.
   *
   * @return remote port
   */
  def remotePort: Int = remoteSocketAddress.getPort

  /**
   * Gets the request parameters as [[ParamMap]] this request has.
   *
   * @return request parameters
   */
  protected def paramsAsParamMap: ParamMap = toUnderlyingAsFinagle.params

  /**
   * Gets the request parameters as [[Map]] this request has.
   *
   * @return request parameters
   */
  def params: Map[String, String] = paramsAsParamMap

  /**
   * Gets the parameter's value as string option this request has.
   *
   * @param name
   * @return parameter's value
   */
  def getParamAsStringOpt(name: String): Option[String] = params.get(name)

  /**
   * Gets the parameter's value as string this request has.
   *
   * @param name
   * @param default default value
   * @return parameter's value
   */
  def getParamAsString(name: String, default: String): String = getParamAsStringOpt(name).getOrElse(default)

  /**
   * Gets the parameter's value as short option this request has.
   *
   * @param name
   * @return parameter's value
   */
  def getParamAsShortOpt(name: String): Option[Short] = paramsAsParamMap.getShort(name)

  /**
   * Gets the parameter's value as short this request has.
   *
   * @param name
   * @param default default value
   * @return parameter's value
   */
  def getParamAsShort(name: String, default: Short): Short = paramsAsParamMap.getShortOrElse(name, default)

  /**
   * Gets the parameter's value as int option this request has.
   *
   * @param name
   * @return parameter's value
   */
  def getParamAsIntOpt(name: String): Option[Int] = paramsAsParamMap.getInt(name)

  /**
   * Gets the parameter's value as int this request has.
   *
   * @param name
   * @param default default value
   * @return parameter's value
   */
  def getParamAsInt(name: String, default: Int): Int = paramsAsParamMap.getIntOrElse(name, default)

  /**
   * Gets the parameter's value as long option this request has.
   *
   * @param name
   * @return parameter's value
   */
  def getParamAsLongOpt(name: String): Option[Long] = paramsAsParamMap.getLong(name)

  /**
   * Gets the parameter's value as long this request has.
   *
   * @param name
   * @param default default value
   * @return parameter's value
   */
  def getParamAsLong(name: String, default: Long): Long = paramsAsParamMap.getLongOrElse(name, default)

  /**
   * Gets the parameter's value as boolean option this request has.
   *
   * @param name
   * @return parameter's value
   */
  def getParamAsBooleanOpt(name: String): Option[Boolean] = paramsAsParamMap.getBoolean(name)

  /**
   * Gets the parameter's value as boolean this request has.
   *
   * @param name
   * @param default default value
   * @return parameter's value
   */
  def getParamAsBoolean(name: String, default: Boolean): Boolean = paramsAsParamMap.getBooleanOrElse(name, default)

  /**
   * Gets the parameter's values as seq this request has.
   *
   * @param name
   * @return parameter's values
   */
  def getParamAsSeq(name: String): Seq[String] = paramsAsParamMap.getAll(name).toList

  /**
   * Gets the parameter's values as key-value seq this request has.
   *
   * @return parameter's keys and values as seq
   */
  def getParams: Seq[(String, String)] = params.toList.map {
    case (k, v) =>
      (k, v)
  }

  /**
   * Gets whether specified parameter contains.
   *
   * @param name
   * @return true if it contains.
   */
  def containsParam(name: String): Boolean = params.contains(name)

  /**
   * Gets the parameter names this request has.
   *
   * @return  parameter names
   */
  def getParamNames: Set[String] = params.keySet

}

/**
 * Represents the companion object for [[Request]].
 */
object Request {

  private[trinity] def apply(request: FinagleRequest): Request =
    new RequestImpl(request)

  /**
   * Creates a [[Request]]'s instance.
   *
   * @param method [[Methods.Value]]
   * @param uri URI
   * @param headers headers
   * @param cookies cookies
   * @param attributes attributes
   * @param content content
   * @param isMutable true if it's mutable
   * @param protocolVersion protocol version
   * @return [[Request]]
   */
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
