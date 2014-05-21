package org.sisioh.trinity.domain.io.http

/**
 * Represents the abstract class for [[ResponseProxy]].
 *
 * @param underlying [[Response]]
 */
abstract class AbstractResponseProxy(val underlying: Response)
  extends ResponseProxy


