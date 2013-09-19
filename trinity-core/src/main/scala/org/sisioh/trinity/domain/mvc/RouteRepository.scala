package org.sisioh.trinity.domain.mvc

import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableByIterable, SyncRepository}

trait RouteRepository
  extends SyncRepository[RouteId, Route[Request, Response]]
  with SyncEntityReadableByIterable[RouteId, Route[Request, Response]]
