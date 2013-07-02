package org.sisioh.trinity.domain.http

import java.io.File
import javax.activation.MimetypesFileTypeMap
import org.sisioh.scala.toolbox.{EnumEntry, Enum}
import org.sisioh.trinity.domain.resource.FileReadFilter

trait ContentType extends EnumEntry {
  val main: String
  val sub: String

  override def equals(obj: Any) = obj match {
    case that: ContentType => main == that.main && sub == that.sub
    case _ => false
  }

  override def toString = "%s/%s".format(main, sub)

}

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

