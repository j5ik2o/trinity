package org.sisioh.trinity

import com.twitter.finagle.http.{Request => FinagleRequest, RequestProxy}
import util.Sorting
import com.google.common.base.Splitter
import scala.collection.JavaConversions._
import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity.domain.{MultiPartItem, ContentType}

case class RequestAdaptor
(rawRequest: FinagleRequest,
 routeParams: Map[String, String] = Map.empty,
 error: Option[Throwable] = None)
  extends RequestProxy with LoggingEx {

  val request = rawRequest

  val multiParams: Map[String, MultiPartItem] =
    if (method == HttpMethod.POST) {
      getContent.markReaderIndex
      val m = MultiPartItem.fromRequest(request)
      getContent.resetReaderIndex()
      m
    } else Map.empty[String, MultiPartItem]

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

}

object AcceptOrdering extends Ordering[String] {

  def getWeight(str: String): Double = {
    val parts = Splitter.on(';').split(str).toArray
    if (parts.length < 2) {
      1.0
    } else {
      try {
        Splitter.on("q=").split(parts(1)).toArray.last.toFloat
      } catch {
        case e: java.lang.NumberFormatException =>
          1.0
      }
    }
  }

  def compare(a: String, b: String) =
    getWeight(b) compare getWeight(a)

}
