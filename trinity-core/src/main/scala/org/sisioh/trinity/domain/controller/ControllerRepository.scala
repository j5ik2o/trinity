package org.sisioh.trinity.domain.controller

import java.util.UUID
import org.sisioh.dddbase.core.{Identity, Repository}

trait ControllerRepository extends Repository[Identity[UUID], Controller]


