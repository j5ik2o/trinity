package org.sisioh.trinity.domain

trait Controller extends Routes {

  implicit protected val config: Config
  implicit protected val pathParser: PathPatternParser

}
