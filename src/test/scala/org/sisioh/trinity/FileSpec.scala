package org.sisioh.trinity

import org.specs2.mutable.Specification

class FileSpec extends Specification {

  "looking up .json" should {
    "recognize application/json" in {
      FileService.getContentType(".json") must_== "application/json"
    }
  }

  "looking up .nonsense" should {
    "default to application/octet-stream" in {
      FileService.getContentType(".nonsense") must_== "application/octet-stream"
    }
  }
}
