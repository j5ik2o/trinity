package org.sisioh.trinity

import com.twitter.util.Future
import com.twitter.finagle.http.{Response => FinagleResponse}

object ResponseAdapter {

  def apply(resp: Future[Response]): Future[FinagleResponse] = {
    resp.map(_.build)
  }

}


