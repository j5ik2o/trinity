package org.sisioh.trinity.domain.io.infrastructure.channel

import org.jboss.netty.channel.{ChannelHandlerContext => NettyChannelHandlerContext}
import org.sisioh.trinity.domain.io.channel.ChannelHandlerContext
import scala.language.implicitConversions

case class ChannelHandlerContextImpl(underlying: NettyChannelHandlerContext) extends ChannelHandlerContext {

}

object ChannelHandlerContextImpl {

  implicit def channelHandlerContextToUnderlying(target: ChannelHandlerContext): NettyChannelHandlerContext =
    target match {
      case ChannelHandlerContextImpl(underlying) => underlying
      case _ => throw new IllegalArgumentException()
    }

}
