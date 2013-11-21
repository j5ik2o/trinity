package org.sisioh.trinity.domain.mvc.http

import org.sisioh.dddbase.core.BaseException

case class RespondNotFoundException(message: Option[String] = None, cause: Option[Throwable] = None)
  extends BaseException(message, cause)

