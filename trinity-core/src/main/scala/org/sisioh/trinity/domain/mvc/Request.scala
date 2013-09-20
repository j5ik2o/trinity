package org.sisioh.trinity.domain.mvc

import com.google.common.base.Splitter
import org.sisioh.trinity.domain.io.transport.codec.http.{Request => IORequest, _}
import scala.collection.JavaConversions._
import scala.util.Sorting

trait Request extends RequestProxy {

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

  def error: Option[Throwable]

  def withError(error: Throwable): this.type

}

object Request {

  def apply(underlying: IORequest): Request =
    new RequestImpl(underlying)

  def apply(version: Version.Value,
            method: Method.Value,
            uri: String,
            routeParams: Map[String, String] = Map.empty): Request =
    new RequestImpl(version, method, uri, routeParams)

}
