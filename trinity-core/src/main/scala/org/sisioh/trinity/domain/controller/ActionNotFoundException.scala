package org.sisioh.trinity.domain.controller

import org.sisioh.dddbase.core.BaseException

case class ActionNotFoundException(message: Option[String] = None, cause: Option[Throwable] = None)
  extends BaseException(message, cause)

