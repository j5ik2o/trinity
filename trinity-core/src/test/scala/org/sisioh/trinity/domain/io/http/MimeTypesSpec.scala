package org.sisioh.trinity.domain.io.http

import org.specs2.mutable.Specification
import java.io.File

class MimeTypesSpec extends Specification {

  "MimeType" should {
    "be able to get html type" in {
      MimeTypes.fileExtensionOf(".html") must_== "text/html"
    }
    "be able to get plain" in {
      MimeTypes.fileOf(new File("src/test/resources/index.txt")) must_== "text/plain"
    }
  }

}
