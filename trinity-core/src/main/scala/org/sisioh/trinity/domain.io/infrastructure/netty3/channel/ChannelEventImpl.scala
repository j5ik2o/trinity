package org.sisioh.trinity.domain.io.infrastructure.netty3.channel

import org.jboss.netty.channel.{ChannelEvent => NettyChannelEvent, ChannelFuture, ChannelFutureListener}
import org.sisioh.trinity.domain.io.channel.ChannelEvent
import scala.concurrent.Promise
import scala.language.implicitConversions
import scala.util.{Failure, Success}

case class ChannelEventImpl(underlying: NettyChannelEvent) extends ChannelEvent {

  def channel = underlying.getChannel

  def future = {
    val promise = Promise[Unit]()
    underlying.getFuture.addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

}

object ChannelEventImpl {

  implicit def toUnderlying(target: ChannelEvent): NettyChannelEvent =
    target match {
      case ChannelEventImpl(underlying) => underlying
      case _ => throw new IllegalArgumentException()
    }

}
