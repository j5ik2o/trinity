package org.sisioh.trinity.domain.io.http

import java.nio.charset.{Charset => JCharset}
import org.sisioh.scala.toolbox._

/**
 * Represents the enumeration entry for a Charset.
 */
trait Charset extends EnumEntry {

  /**
   * Converts this to `java.nio.charset`.
   */
  val toObject: JCharset

}

/**
 * Represents the enumeration for Charsets.
 */
class Charsets extends Enum[Charset] {

  /**
   * Gets the value in the enumeration from charset's name.
   *
   * @param name charset's name
   * @return [[Charset]]
   */
  def valueOf(name: String): Option[Charset] = {
    values.find(_.toObject.toString == name)
  }

  private case class CharsetValue(charsetName: String) extends Charset {
    val toObject = JCharset.forName(charsetName)
  }

  /**
   * UTF-8
   */
  val UTF_8: Charset = CharsetValue("UTF-8")

  /**
   * UTF-16
   */
  val UTF_16: Charset = CharsetValue("UTF-16")

  /**
   * UTF-16 BE
   */
  val UTF_16BE: Charset = CharsetValue("UTF-16BE")

  /**
   * UTF-16 LE
   */
  val UTF_16LE: Charset = CharsetValue("UTF-16LE")

  /**
   * ISO-8859-1
   */
  val ISO_8859_1: Charset = CharsetValue("ISO-8859-1")

  /**
   * US-ASCII
   */
  val US_ASCII: Charset = CharsetValue("US-ASCII")

  val values = defineValues(UTF_8, UTF_16, UTF_16BE, UTF_16LE, ISO_8859_1, US_ASCII)

}

/**
 * Represents then companion object for [[Charsets]].
 */
object Charsets extends Charsets
