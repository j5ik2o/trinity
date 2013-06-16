package org.sisioh.trinity

import org.specs2.mutable.Specification
import org.sisioh.trinity.domain.SinatraPathPatternParser

class PathPatternSpec extends Specification {

  "path" should {
    "hoge" in {
      val r = SinatraPathPatternParser("/path/:userId/hoge/:method")
      println(r("/path/abc/hoge/def"))
      true must_== true
    }
  }

}
