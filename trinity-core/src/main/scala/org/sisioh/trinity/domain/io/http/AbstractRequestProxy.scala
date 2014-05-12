package org.sisioh.trinity.domain.io.http

/**
 * Represents the abstract class for [[RequestProxy]].
 *
 * @param underlying [[Request]]
 */
abstract class AbstractRequestProxy(val underlying: Request)
  extends RequestProxy

