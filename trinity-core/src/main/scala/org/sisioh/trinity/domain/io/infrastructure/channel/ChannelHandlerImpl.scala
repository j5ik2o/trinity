package org.sisioh.trinity.domain.io.infrastructure.channel

import scala.language.implicitConversions
import org.jboss.netty.channel.{ChannelHandler => NettyChannelHandler}
import org.sisioh.trinity.domain.io.channel.ChannelHandler

case class ChannelHandlerImpl(underlying: NettyChannelHandler) extends ChannelHandler {

}

object ChannelHandlerImpl {

  implicit def channelHandlerToUnderlying(target: ChannelHandler): NettyChannelHandler =
    target match {
      case ChannelHandlerImpl(underlying) => underlying
      case _ => throw new IllegalArgumentException()
    }

}
