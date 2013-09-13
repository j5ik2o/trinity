package org.sisioh.trinity.domain.io.infrastructure.netty3.channel

import java.net.SocketAddress
import java.util.UUID
import org.jboss.netty.channel.{Channel => NettyChannel, ChannelFuture, ChannelFutureListener}
import org.sisioh.dddbase.core.model.Identity
import org.sisioh.trinity.domain.io.channel.{ChannelFactory, ChannelConfig, ChannelPipeline, Channel}
import scala.concurrent._
import scala.util._

case class ChannelImpl(underlying: NettyChannel) extends Channel {

  val identity: Identity[UUID] = Identity(UUID.randomUUID())

  def getFactory: ChannelFactory = ???

  def parent: Option[Channel] = Option(underlying.getParent).map(ChannelImpl)

  def config: ChannelConfig = ChannelConfig.from(underlying.getConfig)

  def pipeline: ChannelPipeline = ???

  def isOpen: Boolean = underlying.isOpen

  def isBound: Boolean = underlying.isBound

  def isConnected: Boolean = underlying.isConnected

  def localAddress: Option[SocketAddress] = Option(underlying.getLocalAddress)

  def remoteAddress: Option[SocketAddress] = Option(underlying.getRemoteAddress)

  def write(message: Any): Future[Unit] = {
    val promise = Promise[Unit]()
    underlying.write(message).addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def write(message: Any, remoteAddress: Option[SocketAddress]): Future[Unit] = {
    val promise = Promise[Unit]()
    underlying.write(message, remoteAddress.orNull).addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def bind(localAddress: SocketAddress): Future[Unit] = {
    val promise = Promise[Unit]()
    underlying.bind(localAddress).addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def connect(remoteAddress: SocketAddress): Future[Unit] = {
    val promise = Promise[Unit]()
    underlying.connect(remoteAddress).addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def disconnect: Future[Unit] = {
    val promise = Promise[Unit]()
    underlying.disconnect().addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def unbind: Future[Unit] = {
    val promise = Promise[Unit]()
    underlying.unbind().addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def close(): Future[Unit] = {
    val promise = Promise[Unit]()
    underlying.close().addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def closeFuture: Future[Unit] = {
    val promise = Promise[Unit]()
    underlying.getCloseFuture.addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def interestOps: Int = underlying.getInterestOps

  def isReadable: Boolean = underlying.isReadable

  def isWritable: Boolean = underlying.isWritable

  def setInterestOps(interestOps: Int): Future[Unit] = {
    val promise = Promise[Unit]()
    underlying.setInterestOps(interestOps).addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def setReadable(readable: Boolean): Future[Unit] = {
    val promise = Promise[Unit]()
    underlying.setReadable(readable).addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(()) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def attachment: Option[Any] = synchronized {
    Option(underlying.getAttachment)
  }

  def setAttachment(value: Option[Any])(implicit executor: ExecutionContext): Future[Unit] = future {
    synchronized {
      underlying.setAttachment(value.orNull)
    }
  }

}
