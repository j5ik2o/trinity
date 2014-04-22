package org.sisioh.trinity.domain.io.http

/**
 * Represents the proxy trait for [[Request]].
 */
trait RequestProxy extends Request with MessageProxy {

  override val underlying: Request

  override def method: Methods.Value = underlying.method

  override def withMethod(method: Methods.Value): this.type = mutate {
    _.underlying.withMethod(method)
  }

  override def uri: String = underlying.uri

  override def withUri(uri: String): this.type = mutate {
    _.underlying.withUri(uri)
  }

}

