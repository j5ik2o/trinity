package org.sisioh.trinity.domain.io.http

/**
 * レスポンスのためのプロキシー。
 */
trait ResponseProxy extends Response with MessageProxy {

  def underlying: Response

  def responseStatus: ResponseStatus.Value = underlying.responseStatus

  def withResponseStatus(status: ResponseStatus.Value): this.type = mutate {
    _.underlying.withResponseStatus(status)
  }

}
