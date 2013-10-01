package org.sisioh.trinity.domain.mvc

import java.util.UUID
import org.sisioh.dddbase.core.model.{EntityCloneable, Identity, Entity}


trait Controller[Req, Rep]
  extends Entity[Identity[UUID]]
  with EntityCloneable[Identity[UUID], Controller[Req, Rep]]
  with Ordered[Controller[Req, Rep]] {

  val identity: Identity[UUID] = Identity(UUID.randomUUID())

}
