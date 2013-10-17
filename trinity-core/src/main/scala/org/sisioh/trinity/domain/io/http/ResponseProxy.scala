package org.sisioh.trinity.domain.io.http

trait ResponseProxy extends Response with MessageProxy {

  def underlying: Response

  val status: ResponseStatus.Value = underlying.status

  def withStatus(status: ResponseStatus.Value): this.type = mutate {
    _.underlying.withStatus(status)
  }

}
