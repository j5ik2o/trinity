package org.sisioh.trinity.domain.mvc.http

import org.junit.runner.RunWith
import org.sisioh.trinity.domain.io.http.{ProtocolVersion, ResponseStatus}
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ResponseImplSpec extends Specification {

  "Response" should {
    "be able to get response status" in {
      val response = new ResponseImpl()
      response.responseStatus must_== ResponseStatus.Ok
    }
    "be able to get protocol version" in {
      val response = new ResponseImpl()
      response.protocolVersion must_== ProtocolVersion.Http11
    }
  }

}
