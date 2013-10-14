package org.sisioh.trinity.domain.mvc.routing.pathpattern

/**
 * A Sinatra-compatible route path pattern parser.
 */
case class SinatraPathPatternParser() extends RegexPathPatternParser {

  def apply(pattern: String): PathPattern =
    parseAll(pathPattern, pattern) match {
      case Success(pathPattern, _) =>
        (PartialPathPattern("^") + pathPattern + PartialPathPattern("$")).toPathPattern
      case _ =>
        throw new IllegalArgumentException("Invalid path pattern: " + pattern)
    }

  private def pathPattern = rep(token) ^^ {
    _.reduceLeft {
      _ + _
    }
  }

  private def token = splat | namedGroup | literal

  private def splat = "*" ^^^ PartialPathPattern("(.*?)", List("splat"))

  private def namedGroup = ":" ~> """\w+""".r ^^ {
    groupName => PartialPathPattern("([^/?#]+)", List(groupName))
  }

  private def literal = metaChar | normalChar

  private def metaChar = """[\.\+\(\)\$]""".r ^^ {
    c => PartialPathPattern("\\" + c)
  }

  private def normalChar = ".".r ^^ {
    c => PartialPathPattern(c)
  }
}

object SinatraPathPatternParser {

  def apply(pattern: String): PathPattern =
    new SinatraPathPatternParser().apply(pattern)

}

