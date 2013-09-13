package org.sisioh.trinity.domain.io.channel

import org.jboss.netty.channel.{ChannelHandlerContext => NettyChannelHandlerContext}
import org.sisioh.trinity.domain.io.infrastructure.netty3.channel.ChannelHandlerContextImpl


trait ChannelHandlerContext {

}

object ChannelHandlerContext {

  def from(underlying: NettyChannelHandlerContext): ChannelHandlerContext =
    ChannelHandlerContextImpl(underlying)

}
