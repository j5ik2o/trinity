package org.sisioh.trinity.domain.controller

case class TrinityException
(message: Option[String] = None,
 throwable: Option[Throwable] = None)
  extends Exception(message.orNull, throwable.orNull)


