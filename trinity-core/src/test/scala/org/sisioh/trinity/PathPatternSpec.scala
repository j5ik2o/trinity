package org.sisioh.trinity

import org.specs2.mutable.Specification
import org.sisioh.trinity.domain.routing.{PathPattern, SinatraPathPatternParser}

class PathPatternSpec extends Specification {

  "path" should {
    "hoge" in {
      val r = SinatraPathPatternParser("/path/:userId/hoge/:method")
      println(r("/path/abc/hoge/def"))
      true must_== true
    }
    "test" in {
      val r = PathPattern("""(.*)/(.*)""".r, List("x","y"))
      println("result = " + r("a/b"))
      true must_== true
    }
  }

}
