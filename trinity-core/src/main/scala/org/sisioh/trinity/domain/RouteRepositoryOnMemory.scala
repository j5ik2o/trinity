package org.sisioh.trinity.domain

import org.sisioh.dddbase.core.OnMemoryMutableRepository


class RouteRepositoryOnMemory extends OnMemoryMutableRepository[RouteId, Route] with RouteRepository

