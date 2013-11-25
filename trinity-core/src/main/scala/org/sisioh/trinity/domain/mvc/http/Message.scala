package org.sisioh.trinity.domain.mvc.http

import org.sisioh.trinity.domain.io.http.{Message => IOMessage}
import org.sisioh.trinity.domain.io.http.MessageProxy

import com.twitter.finagle.http.{Message => FinagleMessage}

trait Message extends IOMessage with MessageProxy {

  val toUnderlyingAsFinagle: FinagleMessage


}
