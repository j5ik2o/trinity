package org.sisioh.trinity.domain.mvc

import com.twitter.finagle.http.RequestProxy

trait Request extends RequestProxy {

  val routeParams: Map[String, String]
  val multiParams: Map[String, MultiPartItem]

}
