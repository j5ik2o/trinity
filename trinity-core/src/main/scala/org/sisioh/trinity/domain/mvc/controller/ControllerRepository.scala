package org.sisioh.trinity.domain.mvc.controller

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.sync.SyncRepository
import org.sisioh.dddbase.core.model.Identity

trait ControllerRepository
  extends SyncRepository[Identity[UUID], Controller]


object ControllerRepository {

  def ofMemory: ControllerRepository = ControllerRepositoryOnMemory()

}