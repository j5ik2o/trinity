package org.sisioh.trinity

import com.twitter.finagle.http.{Request => FinagleRequest, RequestProxy}
import util.Sorting
import com.google.common.base.Splitter
import scala.collection.JavaConversions._
import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.scala.toolbox.LoggingEx

/**
 * Adapts a FinagleRquest to a FinatraRequest
 */
object RequestAdaptor extends LoggingEx {

  def apply(rawRequest: FinagleRequest): RequestAdaptor =
    new RequestAdaptor(rawRequest)

}

case class RequestAdaptor(rawRequest: FinagleRequest, routeParams: Map[String, String] = Map.empty, error: Option[Throwable] = None)
  extends RequestProxy {

//  var routeParams: Map[String, String] = Map.empty
  val request = rawRequest

  val multiParams: Map[String, MultipartItem] = if (method == HttpMethod.POST) {
    getContent.markReaderIndex
    val m = MultipartItem.fromRequest(request)
    getContent.resetReaderIndex()
    m
  } else Map.empty[String, MultipartItem]

  def accepts: Seq[ContentType] = {
    val accept = this.getHeader("Accept")
    if (accept != null) {
      val acceptParts = Splitter.on(',').split(accept).toArray
      Sorting.quickSort(acceptParts)(AcceptOrdering)
      val seq = acceptParts.map {
        xs =>
          val part = Splitter.on(";q=").split(xs).toArray.head
          ContentType.valueOf(part).getOrElse(ContentType.All)
      }.toSeq
      seq
    } else {
      Seq.empty[ContentType]
    }
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
