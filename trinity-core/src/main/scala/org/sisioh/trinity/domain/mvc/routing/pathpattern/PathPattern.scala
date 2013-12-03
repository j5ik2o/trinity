/*
 * Copyright 2013 Sisioh Project and others. (http://sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.trinity.domain.mvc.routing.pathpattern

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

/**
 * A path pattern optionally matches a request path and extracts path
 * parameters.
 */
case class PathPattern(regex: Regex, captureGroupNames: Seq[String] = Nil)
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
    this.captureGroupNames.toList ::: pathPattern.captureGroupNames.toList
  )

}
