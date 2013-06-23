package org.sisioh.trinity.view.thymeleaf

object TemplateMode extends Enumeration {
  val XML = Value(1, "XML")
  val XHTML = Value(2, "XHTML")
  val HTML = Value(3, "HTML")
  val HTML5 = Value(4, "HTML5")
  val ValidXML = Value(5, "VALIDXML")
  val ValidXHTML = Value(6, "VALIDXHTML")
  val LegacyHTML5 = Value(7, "LEGACYHTML5")
}
