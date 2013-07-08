package org.sisioh.trinity.test

sealed trait Content

case class MapContent(value: Map[String, String]) extends Content

case class StringContent(value: String) extends Content
