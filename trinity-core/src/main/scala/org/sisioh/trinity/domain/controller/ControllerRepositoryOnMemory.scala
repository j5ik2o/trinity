package org.sisioh.trinity.domain.controller

import java.util.UUID
import org.sisioh.dddbase.core.{OnMemoryMutableRepository, Identity}

class ControllerRepositoryOnMemory
  extends OnMemoryMutableRepository[Identity[UUID], Controller] with ControllerRepository
