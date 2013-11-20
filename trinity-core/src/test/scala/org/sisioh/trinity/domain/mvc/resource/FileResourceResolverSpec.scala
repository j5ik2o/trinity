package org.sisioh.trinity.domain.mvc.resource

import org.specs2.mutable.Specification
import org.sisioh.trinity.domain.mvc.Environment
import java.io.File

class FileResourceResolverSpec extends Specification {
  val localBasePath = new File("trinity-core/src/test/resources/")
  "FileResourceResolver" should {
    "be able to get input stream when development" in {
      val target = FileResourceResolver(Environment.Development, localBasePath)
      target.getInputStream("index.txt") must beSuccessfulTry
    }
    "be able to get input stream when product" in {
      val target = FileResourceResolver(Environment.Product, localBasePath)
      target.getInputStream("index.txt") must beSuccessfulTry
    }
  }
}
