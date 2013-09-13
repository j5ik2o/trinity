package org.sisioh.trinity.domain.io.infrastructure.netty3.channel

import org.jboss.netty.channel.{ChannelPipeline => NettyChannelPipeline, ChannelFuture, ChannelFutureListener}
import org.sisioh.trinity.domain.io.channel._
import scala.collection.JavaConverters._
import scala.concurrent.{Promise, Future}
import scala.util.{Failure, Success}

case class ChannelPipelineImpl(underlying: NettyChannelPipeline) extends ChannelPipeline {

  import ChannelEventImpl._
  import ChannelHandlerImpl._

  def addFirst(name: String, handler: ChannelHandler) {
    underlying.addFirst(name, handler)
  }

  def addLast(name: String, handler: ChannelHandler) {
    underlying.addLast(name, handler)
  }

  def addBefore(baseName: String, name: String, handler: ChannelHandler) {
    underlying.addBefore(baseName, name, handler)
  }

  def addAfter(baseName: String, name: String, handler: ChannelHandler) {
    underlying.addAfter(baseName, name, handler)
  }

  def remove(handler: ChannelHandler) =
    underlying.remove(handler)

  def remove(name: String): ChannelHandler =
    ChannelHandler.from(underlying.remove(name))

  def removeFirst(): ChannelHandler =
    ChannelHandler.from(underlying.removeFirst())

  def removeLast(): ChannelHandler =
    ChannelHandler.from(underlying.removeLast())

  def replace(oldHandler: ChannelHandler, newName: String, newHandler: ChannelHandler) {
    underlying.replace(oldHandler, newName, newHandler)
  }

  def replace(oldName: String, newName: String, newHandler: ChannelHandler): ChannelHandler =
    ChannelHandler.from(underlying.replace(oldName, newName, newHandler))

  def getFirst: Option[ChannelHandler] = Option(underlying.getFirst).map(ChannelHandler.from)

  def getLast: Option[ChannelHandler] = Option(underlying.getLast).map(ChannelHandler.from)

  def get(name: String): Option[ChannelHandler] = Option(underlying.get(name)).map(ChannelHandler.from)

  def getContext(handler: ChannelHandler): ChannelHandlerContext = ChannelHandlerContext.from(underlying.getContext(handler))

  def getContext(name: String): ChannelHandlerContext = ChannelHandlerContext.from(underlying.getContext(name))

  def sendUpstream(event: ChannelEvent) {
    underlying.sendUpstream(event)
  }

  def sendDownstream(event: ChannelEvent) {
    underlying.sendDownstream(event)
  }

  def execute(task: Runnable): Future[Channel] = {
    val promise = Promise[Channel]()
    underlying.execute(task).addListener(new ChannelFutureListener {
      def operationComplete(future: ChannelFuture) {
        promise.complete(if (future.isSuccess) Success(Channel.from(future.getChannel)) else Failure(future.getCause))
      }
    })
    promise.future
  }

  def getChannel: Option[Channel] = Option(Channel.from(underlying.getChannel))

  def getSink: ChannelSink = ???

  def attach(channel: Channel, sink: ChannelSink) {}

  def isAttached: Boolean = underlying.isAttached

  def getNames: Seq[String] = underlying.getNames.asScala.toSeq

  def toMap: Map[String, ChannelHandler] = underlying.toMap.asScala.map {
    e =>
      (e._1, ChannelHandler.from(e._2))
  }.toMap

}
