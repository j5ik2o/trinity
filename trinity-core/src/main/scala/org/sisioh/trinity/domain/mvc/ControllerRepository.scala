package org.sisioh.trinity.domain.mvc

import java.util.UUID
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.dddbase.core.lifecycle.sync.SyncRepository

trait ControllerRepository
  extends SyncRepository[Identity[UUID], Controller[Request, Response]]


object ControllerRepository {

  def ofMemory: ControllerRepository = ControllerRepositoryOnMemory()

}