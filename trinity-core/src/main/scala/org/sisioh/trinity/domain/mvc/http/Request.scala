package org.sisioh.trinity.domain.mvc.http

import java.net.InetAddress
import java.net.InetSocketAddress
import scala.collection.JavaConversions.iterableAsScalaIterable
import scala.concurrent.Future
import scala.util.Sorting
import org.sisioh.trinity.domain.io.http.AcceptOrdering
import org.sisioh.trinity.domain.io.http.ContentType
import org.sisioh.trinity.domain.io.http.Method
import org.sisioh.trinity.domain.io.http.{Request => IORequest}
import org.sisioh.trinity.domain.io.http.RequestProxy
import org.sisioh.trinity.domain.io.http.Version
import org.sisioh.trinity.domain.mvc.GlobalSettings
import org.sisioh.trinity.domain.mvc.action.Action
import com.google.common.base.Splitter
import com.twitter.finagle.http.ParamMap
import com.twitter.finagle.http.{Request => FinagleRequest}
import org.sisioh.scala.toolbox.LoggingEx

trait Request extends Message with RequestProxy with LoggingEx {

  val actionOpt: Option[Action[Request, Response]]

  def withAction(action: Option[Action[Request, Response]]): this.type

  def encodeBytes: Array[Byte] = finagle.encodeBytes()

  def encodeString: String = finagle.encodeString()

  def remoteSocketAddress: InetSocketAddress = finagle.remoteSocketAddress

  def remoteHost: String = remoteAddress.getHostAddress

  def remoteAddress: InetAddress = remoteSocketAddress.getAddress

  def remotePort: Int = remoteSocketAddress.getPort

  private def _params = finagle.params

  def params: Map[String, String] = _params

  def path: String = finagle.path

  def fileExtension = finagle.fileExtension

  val routeParams: Map[String, String]

  def withRouteParams(routeParams: Map[String, String]): this.type

  val multiParams: Map[String, MultiPartItem]

  def accepts: Seq[ContentType] = {
    val acceptOpt = getHeader("Accept")
    acceptOpt.map {
      accept =>
        val acceptParts = Splitter.on(',').split(accept).toArray
        Sorting.quickSort(acceptParts)(AcceptOrdering)
        acceptParts.map {
          xs =>
            val part = Splitter.on(";q=").split(xs).toArray.head
            ContentType.valueOf(part).getOrElse(ContentType.All)
        }.toSeq
    }.getOrElse(Seq.empty[ContentType])
  }

  val errorOpt: Option[Throwable]

  def withError(error: Throwable): this.type

  def execute(defaultAction: Action[Request, Response]): Future[Response] = withDebugScope(s"${toString}: execute") {
    actionOpt.map(_(this)).getOrElse(defaultAction(this))
  }

  val globalSettingsOpt: Option[GlobalSettings[Request, Response]]

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

  def fromUnderlying(underlying: IORequest,
                     action: Option[Action[Request, Response]] = None,
                     routeParams: Map[String, String] = Map.empty,
                     globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None,
                     errorOpt: Option[Throwable] = None): Request =
    new RequestImpl(underlying, action, routeParams, globalSettingsOpt, errorOpt)

  def apply(method: Method.Value = Method.Get,
            uri: String = "/",
            action: Option[Action[Request, Response]] = None,
            routeParams: Map[String, String] = Map.empty,
            globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None,
            errorOpt: Option[Throwable] = None,
            version: Version.Value = Version.Http11): Request =
    new RequestImpl(method, uri, action, routeParams, globalSettingsOpt, errorOpt, version)

}
