package org.sisioh.trinity.domain.mvc

import java.util.UUID
import org.sisioh.dddbase.core.model.{Identity, Entity}

trait Controller[-Req, +Rep] extends Entity[Identity[UUID]]{

}
