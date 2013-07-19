package org.sisioh.trinity.domain.routing

import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableByIterable, SyncRepository}

trait RouteRepository
  extends SyncRepository[RouteId, Route]
  with SyncEntityReadableByIterable[RouteId, Route]


