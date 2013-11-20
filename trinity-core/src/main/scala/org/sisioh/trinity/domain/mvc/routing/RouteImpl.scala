package org.sisioh.trinity.domain.mvc.routing

import org.sisioh.trinity.domain.mvc.action.Action

private[domain]
class RouteImpl[Req, Rep]
(val identity: RouteId, val action: Action[Req, Rep]) extends Route[Req, Rep]
