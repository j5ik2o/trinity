package org.sisioh.trinity.domain.io.channel

import org.jboss.netty.channel.{ChannelHandler => NettyChannelHandler}
import org.sisioh.trinity.domain.io.infrastructure.channel.ChannelHandlerImpl

trait ChannelHandler {

}

object ChannelHandler {

  def from(underlying: NettyChannelHandler): ChannelHandler =
    ChannelHandlerImpl(underlying)

}
