package org.sisioh.trinity.domain.io.http

/**
 * Represents the proxy trait for [[Response]].
 */
trait ResponseProxy extends Response with MessageProxy {

  def underlying: Response

  def responseStatus: ResponseStatus.Value = underlying.responseStatus

  def withResponseStatus(status: ResponseStatus.Value): this.type = mutate {
    _.underlying.withResponseStatus(status)
  }

}
