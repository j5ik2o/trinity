package org.sisioh.trinity.domain.mvc

import java.util.UUID
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity, Entity}


trait Controller
  extends Entity[Identity[UUID]]
  with EntityCloneable[Identity[UUID], Controller]
  with Ordered[Controller] {

  val identity: Identity[UUID] = Identity(UUID.randomUUID())

  def compare(that: Controller): Int =
    identity.value.compareTo(that.identity.value)

}
