package org.sisioh.trinity.domain.mvc

import org.sisioh.trinity.domain.io.Service
import scala.concurrent.Future

class ControllerServiceImpl[-Req, +Rep] extends Service[Req, Rep] {

  def apply(request: Req): Future[Rep] = {
    null
  }

}
