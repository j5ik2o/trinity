package org.sisioh.trinity.domain.routing

import org.sisioh.dddbase.core.lifecycle.{EntityReaderByIterable, Repository}

trait RouteRepository extends Repository[RouteRepository, RouteId, Route] with EntityReaderByIterable[RouteId, Route]


