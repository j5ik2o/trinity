package org.sisioh.trinity.domain.controller

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.Repository
import org.sisioh.dddbase.core.model.Identity

trait ControllerRepository
  extends Repository[ControllerRepository, Identity[UUID], Controller]


