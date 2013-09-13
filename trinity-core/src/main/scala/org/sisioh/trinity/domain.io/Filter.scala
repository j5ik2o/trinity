package org.sisioh.trinity.domain

import scala.concurrent.Future

trait Filter[-ReqIn, +RepOut, +ReqOut, -RepIn]
  extends ((ReqIn, Service[ReqOut, RepIn]) => Future[RepOut]) {

  def apply(request: ReqIn, service: Service[ReqOut, RepIn]): Future[RepOut]

}
