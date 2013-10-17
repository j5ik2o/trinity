package org.sisioh.trinity.domain.mvc.http

import java.net.InetAddress
import java.net.InetSocketAddress
import scala.collection.JavaConversions.iterableAsScalaIterable
import scala.concurrent.Future
import scala.util.Sorting
import org.sisioh.trinity.domain.io.http.AcceptOrdering
import org.sisioh.trinity.domain.io.http.ContentType
import org.sisioh.trinity.domain.io.http.Method
import org.sisioh.trinity.domain.io.http.{ Request => IORequest }
import org.sisioh.trinity.domain.io.http.RequestProxy
import org.sisioh.trinity.domain.io.http.Version
import org.sisioh.trinity.domain.mvc.GlobalSettings
import org.sisioh.trinity.domain.mvc.action.Action
import com.google.common.base.Splitter
import com.twitter.finagle.http.ParamMap
import com.twitter.finagle.http.{ Request => FinagleRequest }
import org.sisioh.scala.toolbox.LoggingEx

trait Request extends Message with RequestProxy with LoggingEx {

  lazy val finagle = FinagleRequest(netty)

  val actionOpt: Option[Action[Request, Response]]

  def withAction(action: Option[Action[Request, Response]]): this.type

  def encodeBytes: Array[Byte] = finagle.encodeBytes()

  def encodeString: String = finagle.encodeString()

  def remoteSocketAddress: InetSocketAddress = finagle.remoteSocketAddress

  def remoteHost: String = remoteAddress.getHostAddress

  def remoteAddress: InetAddress = remoteSocketAddress.getAddress

  def remotePort: Int = remoteSocketAddress.getPort

  def params: ParamMap = finagle.params

  def path: String = finagle.path

  def fileExtension = finagle.fileExtension

  val routeParams: Map[String, String]

  def withRouteParams(routeParams: Map[String, String]): this.type

  val multiParams: Map[String, MultiPartItem]

  def accepts: Seq[ContentType] = {
    val acceptOpt = Option(getHeader("Accept"))
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

  def getParam(name: String, default: String): String = params.get(name).getOrElse(default)

  def getShortParam(name: String): Short = params.getShortOrElse(name, 0)

  def getShortParam(name: String, default: Short): Short = params.getShortOrElse(name, default)

  def getIntParam(name: String): Int = params.getIntOrElse(name, 0)

  def getIntParam(name: String, default: Int): Int = params.getIntOrElse(name, default)

  def getLongParam(name: String): Long = params.getLongOrElse(name, 0L)

  def getLongParam(name: String, default: Long = 0L): Long = params.getLongOrElse(name, default)

  def getBooleanParam(name: String): Boolean = params.getBooleanOrElse(name, false)

  def getBooleanParam(name: String, default: Boolean): Boolean = params.getBooleanOrElse(name, default)

  def getParams(name: String): Seq[String] = params.getAll(name).toList

  def getParams(): Seq[(String, String)] = params.toList.map {
    case (k, v) =>
      (k, v)
  }

  def containsParam(name: String): Boolean = params.contains(name)

  def getParamNames(): Set[String] = params.keySet

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
