package org.sisioh.trinity.domain.mvc

import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport

class RouteRepositoryOnMemory
  extends SyncRepositoryOnMemorySupport[RouteId, Route[Request, Response]]
  with RouteRepository {

  type This = RouteRepositoryOnMemory

}
