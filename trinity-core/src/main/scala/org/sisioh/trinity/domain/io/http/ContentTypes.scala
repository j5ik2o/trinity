package org.sisioh.trinity.domain.io.http

import org.sisioh.scala.toolbox.Enum
import org.sisioh.scala.toolbox.EnumEntry

/**
 * Represents the enumeration entry for content type.
 */
trait ContentType extends EnumEntry {

  /**
   * main type name.
   */
  val main: String

  /**
   * sub type name.
   */
  val sub: Option[String]

  override def equals(obj: Any) = obj match {
    case that: ContentType =>
      main == that.main && sub == that.sub
    case _ => false
  }

  override def toString = "%s/%s".format(main, sub.getOrElse("*"))

}

/**
 * Represents the enumeration for [[ContentType]].
 */
class ContentTypes extends Enum[ContentType] {

  /**
   * Gets a any [[ContentType]].
   *
   * @param main main type name
   * @param sub sub type name
   * @return [[ContentType]]
   */
  def ofAny(main: String, sub: Option[String]): ContentType = AnyContentType(main, sub)

  /**
   * Gets a any [[ContentType]].
   *
   * @param value content type name as string(like "main/sub", "main")
   * @return [[ContentType]]
   */
  def ofAny(value: String): ContentType = {
    val s = value.split("/")
    if (s.size > 0) {
      AnyContentType(s(0), Some(s(1)))
    } else {
      AnyContentType(value, None)
    }
  }

  /**
   * Gets the enumeration entry.
   *
   * @param value content type name as string
   * @return [[ContentType]]
   */
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

  private case class AnyContentType(main: String, sub: Option[String]) extends ContentType

  val values = defineValues(TextPlan, TextHtml, AppJson, AppXml, AppRss, AppOctetStream, AppOctetStream, All)

}

/**
 * Represents the companion object for [[ContentTypes]].
 */
object ContentTypes extends ContentTypes