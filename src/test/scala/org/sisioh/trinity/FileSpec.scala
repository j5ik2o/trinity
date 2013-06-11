package org.sisioh.trinity

import org.specs2.mutable.Specification

class FileSpec extends Specification {

  "looking up .json" should {
    "recognize application/json" in {
      ContentType.getContentType(".json") must_== "application/json"
    }
  }

  "looking up .nonsense" should {
    "default to application/octet-stream" in {
      ContentType.getContentType(".nonsense") must_== "application/octet-stream"
    }
  }
}
