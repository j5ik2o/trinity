/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
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
package org.sisioh.trinity.domain.http

import java.io.File
import javax.activation.MimetypesFileTypeMap
import org.sisioh.scala.toolbox.{EnumEntry, Enum}
import org.sisioh.trinity.domain.resource.FileReadFilter

/**
 * コンテントタイプを表すトレイト。
 */
trait ContentType extends EnumEntry {
  val main: String
  val sub: String

  override def equals(obj: Any) = obj match {
    case that: ContentType =>
      main == that.main && sub == that.sub
    case _ => false
  }

  override def toString = "%s/%s".format(main, sub)

}

/**
 * コンパニオンオブジェクト。
 */
object ContentType extends Enum[ContentType] {

  def getContentType(str: String): String = {
    extMap.getContentType(str)
  }

  def getContentType(file: File): String = {
    extMap.getContentType(file)
  }

  private lazy val extMap = new MimetypesFileTypeMap(FileReadFilter.getClass.getResourceAsStream("/META-INF/mime.types"))

  def valueOf(value: String): Option[ContentType] = {
    values.find(_.toString() == value)
  }

  case object TextPlan extends ContentType {
    val main: String = "text"
    val sub: String = "plan"
  }

  case object TextHtml extends ContentType {
    val main: String = "text"
    val sub: String = "html"
  }

  case object AppJson extends ContentType {
    val main: String = "application"
    val sub: String = "json"
  }

  case object AppXml extends ContentType {
    val main: String = "application"
    val sub: String = "xml"
  }

  case object AppRss extends ContentType {
    val main: String = "application"
    val sub: String = "rss"
  }

  case object AppOctetStream extends ContentType {
    val main: String = "application"
    val sub: String = "octet-stream"
  }

  case object All extends ContentType {
    val main: String = "*"
    val sub: String = "*"
  }

  case object Unsupported extends ContentType {
    val main = ""
    val sub = ""
  }

  TextPlan % TextHtml % AppJson % AppXml % AppRss % AppOctetStream % AppOctetStream % All

}

