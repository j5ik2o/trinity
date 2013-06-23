package org.sisioh.trinity.domain.controller

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.mutable.OnMemoryRepositorySupport
import org.sisioh.dddbase.core.model.Identity

class ControllerRepositoryOnMemory
  extends OnMemoryRepositorySupport[ControllerRepositoryOnMemory, Identity[UUID], Controller] with ControllerRepository
