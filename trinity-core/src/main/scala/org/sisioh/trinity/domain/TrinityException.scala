package org.sisioh.trinity.domain

import org.sisioh.dddbase.core.BaseException

case class TrinityException(message: Option[String] = None, cause: Option[Throwable] = None)
  extends BaseException(message, cause)
