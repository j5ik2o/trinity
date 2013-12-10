/**
 * Scalatra is distributed under the following terms:
 *
 * Copyright (c) Alan Dipert <alan.dipert@gmail.com>. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
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
