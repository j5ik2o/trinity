package org.sisioh.trinity.domain.mvc.routing.pathpattern

/**
 * A Sinatra-compatible route path pattern parser.
 */
case class SinatraPathPatternParser() extends RegexPathPatternParser {

  def apply(pattern: String): PathPattern =
    parseAll(pathPattern, pattern) match {
      case Success(pr, _) =>
        (PartialPathPattern("^") + pr + PartialPathPattern("$")).toPathPattern
      case _ =>
        throw new IllegalArgumentException("Invalid path pattern: " + pattern)
    }

  private lazy val pathPattern = rep(token) ^^ {
    _.reduceLeft {
      _ + _
    }
  }

  private lazy val token = splat | namedGroup | literal

  private lazy val splat = "*" ^^^ PartialPathPattern("(.*?)", List("splat"))

  private lazy val namedGroup = ":" ~> """\w+""".r ^^ {
    groupName => PartialPathPattern("([^/?#]+)", List(groupName))
  }

  private lazy val literal = metaChar | normalChar

  private lazy val metaChar = """[\.\+\(\)\$]""".r ^^ {
    c => PartialPathPattern("\\" + c)
  }

  private lazy val normalChar = ".".r ^^ {
    c => PartialPathPattern(c)
  }
}

object SinatraPathPatternParser {

  def apply(pattern: String): PathPattern =
    new SinatraPathPatternParser().apply(pattern)

}

