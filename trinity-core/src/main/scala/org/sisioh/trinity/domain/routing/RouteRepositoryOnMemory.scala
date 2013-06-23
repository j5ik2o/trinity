package org.sisioh.trinity.domain.routing

import org.sisioh.dddbase.core.lifecycle.memory.mutable.OnMemoryRepositorySupport

class RouteRepositoryOnMemory extends OnMemoryRepositorySupport[RouteRepositoryOnMemory,RouteId, Route] with RouteRepository

