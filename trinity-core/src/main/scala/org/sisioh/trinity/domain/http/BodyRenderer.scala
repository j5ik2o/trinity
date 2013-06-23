package org.sisioh.trinity.domain.http

import com.twitter.util.Future


trait BodyRenderer {

  def render: Future[String]

}
