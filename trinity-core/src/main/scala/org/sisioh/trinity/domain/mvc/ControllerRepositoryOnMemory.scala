package org.sisioh.trinity.domain.mvc

import org.sisioh.dddbase.core.model.Identity
import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport

private[mvc]
case class ControllerRepositoryOnMemory()
  extends SyncRepositoryOnMemorySupport[Identity[UUID], Controller[Request, Response]]
  with ControllerRepository {

  type This = ControllerRepositoryOnMemory

}

