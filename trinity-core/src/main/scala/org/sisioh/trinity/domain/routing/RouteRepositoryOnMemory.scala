package org.sisioh.trinity.domain.routing

import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport

class RouteRepositoryOnMemory
  extends SyncRepositoryOnMemorySupport[RouteId, Route] with RouteRepository {

  type This = RouteRepositoryOnMemory

}

