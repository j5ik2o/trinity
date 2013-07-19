package org.sisioh.trinity.domain.routing

import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport

/**
 * [[org.sisioh.trinity.domain.routing.Route]]のためのリポジトリ実装。
 */
class RouteRepositoryOnMemory
  extends SyncRepositoryOnMemorySupport[RouteId, Route]
  with RouteRepository {

  type This = RouteRepositoryOnMemory

}

