package org.sisioh.trinity.domain.io.channel

import java.net.SocketAddress
import java.util.UUID

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import org.jboss.netty.channel.{Channel => NettyChannel}
import org.sisioh.dddbase.core.model.Entity
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.trinity.domain.io.infrastructure.channel.ChannelImpl

trait Channel extends Entity[Identity[UUID]] {

  def getFactory: ChannelFactory

  def parent: Option[Channel]

  def config: ChannelConfig

  def pipeline: ChannelPipeline

  def isOpen: Boolean

  def isBound: Boolean

  def isConnected: Boolean

  def localAddress: Option[SocketAddress]

  def remoteAddress: Option[SocketAddress]

  def write(message: Any): Future[Unit]

  def write(message: Any, remoteAddress: Option[SocketAddress]): Future[Unit]

  def bind(localAddress: SocketAddress): Future[Unit]

  def connect(remoteAddress: SocketAddress): Future[Unit]

  def disconnect: Future[Unit]

  def unbind: Future[Unit]

  def close(): Future[Unit]

  def closeFuture: Future[Unit]

  def interestOps: Int

  def isReadable: Boolean

  def isWritable: Boolean

  def setInterestOps(interestOps: Int): Future[Unit]

  def setReadable(readable: Boolean): Future[Unit]

  def attachment: Option[Any]

  def setAttachment(value: Option[Any])(implicit executor: ExecutionContext): Future[Unit]

}

object Channel {

  def from(underlying: NettyChannel): Channel = ChannelImpl(underlying)

}