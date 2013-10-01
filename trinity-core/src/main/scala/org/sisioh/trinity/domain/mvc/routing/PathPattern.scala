package org.sisioh.trinity.domain.mvc.routing

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
import scala.collection.mutable

/**
 * A path pattern optionally matches a request path and extracts path
 * parameters.
 */
case class PathPattern(regex: Regex, captureGroupNames: List[String] = Nil)
  extends Ordered[PathPattern] {

  def compare(that: PathPattern): Int = {
    regex.toString().compare(that.regex.toString())
  }

  def apply(path: String): Option[Map[String, String]] = {
    regex.findFirstMatchIn(path).map {
      captureGroupNames zip _.subgroups
    }.map {
      pairs =>
        val multiParams = new mutable.HashMap[String, ListBuffer[String]]
        pairs foreach {
          case (k, v) =>
            if (v != null)
              multiParams.getOrElseUpdate(k, ListBuffer[String]()) += v
        }
        multiParams.map {
          case (k, v) => (k, v.result().head)
        }.toMap
    }
  }

  def +(pathPattern: PathPattern): PathPattern = PathPattern(
    new Regex(this.regex.toString + pathPattern.regex.toString),
    this.captureGroupNames ::: pathPattern.captureGroupNames
  )

}
