package org.sisioh.trinity.domain.mvc

import com.google.common.base.Splitter
import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest, _}
import scala.collection.JavaConversions._
import scala.util.Sorting
import scala.concurrent.Future

trait Request extends RequestProxy {

  val action: Option[Action[Request, Response]]

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

  def path: String

  val errorOpt: Option[Throwable]

  def withError(error: Throwable): this.type

  def execute: Future[Response] = action.map(_(this)).getOrElse(notFoundHandler(this))

  val globalSettingsOpt: Option[GlobalSettings[Request, Response]]

  /**
   * アクションが見つからない場合のリカバリを行うためのハンドラ。
   *
   * @return `Future`にラップされた[[com.twitter.finagle.http.Request]]
   */
  protected def notFoundHandler(request: Request): Future[Response] = {
    globalSettingsOpt.map {
      _.notFound.map(_(request)).
        getOrElse(NotFoundHandleAction(request))
    }.getOrElse {
      NotFoundHandleAction(request)
    }
  }

}

object Request {

  def fromUnderlying(underlying: IORequest,
            action: Option[Action[Request, Response]] = None,
            routeParams: Map[String, String] = Map.empty,
            globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None,
            errorOpt: Option[Throwable] = None): Request =
    new RequestImpl(underlying, action, routeParams, globalSettingsOpt, errorOpt)

  def apply(method: Method.Value,
            uri: String,
            action: Option[Action[Request, Response]] = None,
            routeParams: Map[String, String] = Map.empty,
            globalSettingsOpt: Option[GlobalSettings[Request, Response]] = None,
            errorOpt: Option[Throwable] = None,
            version: Version.Value = Version.Http11): Request =
    new RequestImpl(method, uri, action, routeParams, globalSettingsOpt, errorOpt, version)

}
