package org.sisioh.trinity.domain.mvc.resource

import java.io.File
import org.sisioh.trinity.domain.mvc.Environment
import org.specs2.mutable.Specification

class FileResourceResolverSpec extends Specification {
  val current = new File(".").getAbsoluteFile
  val localBasePath =  if (current.toString.endsWith("trinity-core/.")) {
    new File(current, "src/test/resources/")
  } else {
    new File(current, "trinity-core/src/test/resources/")
  }
  "FileResourceResolver" should {
    "be able to get input stream when development" in {
      val target = FileResourceResolver(Environment.Development, localBasePath)
      val fh = target.getInputStream("/index.txt")
      val r = fh must beSuccessfulTry
      fh.get.close()
      r
    }
    "be able to get input stream when product" in {
      val target = FileResourceResolver(Environment.Product, localBasePath)
      val fh = target.getInputStream("/index.txt")
      val r = fh must beSuccessfulTry
      fh.get.close()
      r
    }
  }
}
