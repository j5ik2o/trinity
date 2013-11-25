package org.sisioh.trinity.domain.io.http

trait RequestProxy extends Request with MessageProxy {

  val underlying: Request

  def method: Methods.Value = underlying.method

  def withMethod(method: Methods.Value): this.type = mutate {
    _.underlying.withMethod(method)
  }

  def uri: String = underlying.uri

  def withUri(uri: String): this.type = mutate {
    _.underlying.withUri(uri)
  }

}

