package org.sisioh.trinity.domain.controller

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport
import org.sisioh.dddbase.core.model.Identity

/**
 * [[org.sisioh.trinity.domain.controller.Controller]]のためのリポジトリ実装。
 */
class ControllerRepositoryOnMemory
  extends SyncRepositoryOnMemorySupport[Identity[UUID], Controller]
  with ControllerRepository {

  type This = ControllerRepositoryOnMemory

}
