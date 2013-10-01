package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport
import org.sisioh.trinity.domain.mvc.{Response, Request}

private[routing]
case class RouteRepositoryOnMemory()
  extends SyncRepositoryOnMemorySupport[RouteId, Route[Request, Response]]
  with RouteRepository {

  type This = RouteRepositoryOnMemory

}
