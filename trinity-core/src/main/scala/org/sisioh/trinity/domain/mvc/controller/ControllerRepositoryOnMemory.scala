package org.sisioh.trinity.domain.mvc.controller

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport
import org.sisioh.dddbase.core.model.Identity

private[mvc]
case class ControllerRepositoryOnMemory()
  extends SyncRepositoryOnMemorySupport[Identity[UUID], Controller]
  with ControllerRepository {

  type This = ControllerRepositoryOnMemory

}

