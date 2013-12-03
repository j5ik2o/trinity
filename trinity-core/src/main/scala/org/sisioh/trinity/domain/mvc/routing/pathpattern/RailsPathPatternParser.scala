package org.sisioh.trinity.domain.mvc.routing.pathpattern

/**
 * Path pattern parser based on Rack::Mount::Strexp, which is used by Rails.
 */
case class RailsPathPatternParser() extends RegexPathPatternParser {

  def apply(pattern: String): PathPattern =
    parseAll(target, pattern) match {
      case Success(t, _) => t
      case _ =>
        throw new IllegalArgumentException("Invalid path pattern: " + pattern)
    }

  private lazy val target = expr ^^ {
    e => PartialPathPattern("\\A" + e.regex + "\\Z", e.captureGroupNames).toPathPattern
  }

  private lazy val expr = rep1(token) ^^ {
    _.reduceLeft {
      _ + _
    }
  }

  private lazy val token = param | glob | optional | static

  private lazy val param = ":" ~> identifier ^^ {
    name => PartialPathPattern("([^#/.?]+)", List(name))
  }

  private lazy val identifier = """[a-zA-Z_]\w*""".r

  private lazy val glob = "*" ~> identifier ^^ {
    name => PartialPathPattern("(.+)", List(name))
  }

  private lazy val optional: Parser[PartialPathPattern] = "(" ~> expr <~ ")" ^^ {
    e => PartialPathPattern("(?:" + e.regex + ")?", e.captureGroupNames)
  }

  private lazy val static = (escaped | char) ^^ {
    str => PartialPathPattern(str)
  }

  private lazy val escaped = literal("\\") ~> (char | paren)

  private lazy val char = metachar | stdchar

  private lazy val metachar = """[.^$|?+*{}\\\[\]-]""".r ^^ {
    "\\" + _
  }

  private lazy val stdchar = """[^()]""".r

  private lazy val paren = ("(" | ")") ^^ {
    "\\" + _
  }
}

object RailsPathPatternParser {
  def apply(pattern: String): PathPattern = new RailsPathPatternParser().apply(pattern)
}
