package org.sisioh.trinity.domain.io.http

import org.sisioh.scala.toolbox.Enum
import org.sisioh.scala.toolbox.EnumEntry

/**
 * コンテントタイプを表すトレイト。
 */
trait ContentType extends EnumEntry {
  val main: String
  val sub: Option[String]

  override def equals(obj: Any) = obj match {
    case that: ContentType =>
      main == that.main && sub == that.sub
    case _ => false
  }

  override def toString = "%s/%s".format(main, sub.getOrElse("*"))

}

/**
 * コンパニオンオブジェクト。
 */
object ContentType extends Enum[ContentType] {

  def valueOf(value: String): Option[ContentType] = {
    values.find(_.toString() == value)
  }

  case object TextPlan extends ContentType {
    val main = "text"
    val sub = Some("plain")
  }

  case object TextHtml extends ContentType {
    val main = "text"
    val sub = Some("html")
  }

  case object AppJson extends ContentType {
    val main = "application"
    val sub = Some("json")
  }

  case object AppXml extends ContentType {
    val main = "application"
    val sub = Some("xml")
  }

  case object AppRss extends ContentType {
    val main = "application"
    val sub = Some("rss")
  }

  case object AppOctetStream extends ContentType {
    val main = "application"
    val sub = Some("octet-stream")
  }

  case object All extends ContentType {
    val main = "*"
    val sub = None
  }

  case object Unsupported extends ContentType {
    val main = throw new UnsupportedOperationException
    val sub = throw new UnsupportedOperationException
  }

  TextPlan % TextHtml % AppJson % AppXml % AppRss % AppOctetStream % AppOctetStream % All

}
