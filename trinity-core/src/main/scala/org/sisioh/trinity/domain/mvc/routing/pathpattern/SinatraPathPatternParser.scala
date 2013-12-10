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

