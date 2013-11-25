package org.sisioh.trinity.domain.io.http

import java.nio.charset.{Charset => JCharset}
import org.sisioh.scala.toolbox._

trait Charset extends EnumEntry {
  val toObject: JCharset
}

object Charsets extends Enum[Charset] {

  def valueOf(name: String): Option[Charset] = {
    values.find(_.toObject.toString == name)
  }

  private case class CharsetValue(charsetName: String) extends Charset {
    val toObject = JCharset.forName(charsetName)
  }

  val UTF_8: Charset = CharsetValue("UTF-8")
  val UTF_16: Charset = CharsetValue("UTF-16")
  val UTF_16BE: Charset = CharsetValue("UTF-16BE")
  val UTF_16LE: Charset = CharsetValue("UTF-16LE")
  val ISO_8859_1: Charset = CharsetValue("ISO-8859-1")
  val US_ASCII: Charset = CharsetValue("US-ASCII")

  UTF_8 % UTF_16 % UTF_16BE % UTF_16LE % ISO_8859_1 % US_ASCII

}
