package org.sisioh.trinity.domain.io.channel

import org.jboss.netty.channel.{ChannelPipeline => NettyChannelPipeline}
import org.sisioh.trinity.domain.io.infrastructure.netty3.channel.ChannelPipelineImpl
import scala.concurrent.Future

trait ChannelPipeline {

  def addFirst(name: String, handler: ChannelHandler): Unit

  def addLast(name: String, handler: ChannelHandler): Unit

  def addBefore(baseName: String, name: String, handler: ChannelHandler): Unit

  def addAfter(baseName: String, name: String, handler: ChannelHandler): Unit

  def remove(handler: ChannelHandler): Unit

  def remove(name: String): ChannelHandler

  def removeFirst(): ChannelHandler

  def removeLast(): ChannelHandler

  def replace(oldHandler: ChannelHandler, newName: String, newHandler: ChannelHandler): Unit

  def replace(oldName: String, newName: String, newHandler: ChannelHandler): ChannelHandler


  def getFirst: Option[ChannelHandler]

  def getLast: Option[ChannelHandler]

  def get(name: String): Option[ChannelHandler]


  def getContext(handler: ChannelHandler): ChannelHandlerContext

  def getContext(name: String): ChannelHandlerContext

  def sendUpstream(event: ChannelEvent): Unit

  def sendDownstream(event: ChannelEvent): Unit

  def execute(task: Runnable): Future[Channel]

  def getChannel: Option[Channel]

  def getSink: ChannelSink

  def attach(channel: Channel, sink: ChannelSink): Unit

  def isAttached: Boolean

  def getNames: Seq[String]

  def toMap: Map[String, ChannelHandler]

}

object ChannelPipeline {

  def from(underlying: NettyChannelPipeline): ChannelPipeline =
    ChannelPipelineImpl(underlying)


}