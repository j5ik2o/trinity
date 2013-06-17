package org.sisioh.trinity

import com.google.common.base.Splitter
import scala.util.Sorting
import org.specs2.mutable.Specification
import scala.collection.JavaConversions._
import org.sisioh.trinity.domain.AcceptOrdering

class RequestSpec extends Specification {
  "AcceptOrdering" should {
    "understand accept header ordering" in {
      val accept = "application/xhtml+xml;q=2,application/xml;q=0.9,*/*;q=0.8,text/html;q=0.2"
      val parts = Splitter.on(',').split(accept).toArray
      Sorting.quickSort(parts)(AcceptOrdering)
      parts(3) must_== ("text/html;q=0.2")
      parts(2) must_== ("*/*;q=0.8")
      parts(1) must_== "application/xml;q=0.9"
      parts(0) must_== ("application/xhtml+xml;q=2")
    }
  }
}
