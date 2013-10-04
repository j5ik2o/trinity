package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.dddbase.core.lifecycle.sync.{SyncEntityReadableByIterable, SyncRepository}
import org.sisioh.trinity.domain.mvc.http.{Response, Request}

trait RouteRepository
  extends SyncRepository[RouteId, Route[Request, Response]]
  with SyncEntityReadableByIterable[RouteId, Route[Request, Response]]

object RouteRepository {

  def ofMemory: RouteRepository = RouteRepositoryOnMemory()


}
