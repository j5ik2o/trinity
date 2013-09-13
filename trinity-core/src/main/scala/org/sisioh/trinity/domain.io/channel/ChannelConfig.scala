package org.sisioh.trinity.domain.io.channel

import org.jboss.netty.channel.{ChannelConfig => NettyChannelConfig, ChannelPipelineFactory}
import org.sisioh.trinity.domain.io.buffer.ChannelBufferFactory
import org.sisioh.trinity.domain.io.infrastructure.netty3.channel.ChannelConfigImpl

trait ChannelConfig {

  def setOptions(options: Map[String, Any]): Unit

  def setOption(name: String, value: Any): Unit

  def bufferFactory: ChannelBufferFactory

  def bufferFactory_=(value: ChannelBufferFactory): Unit

  def pipelineFactory: ChannelPipelineFactory

  def pipelineFactory_=(value: ChannelPipelineFactory): Unit

  def connectTimeoutMillis: Int

  def connectTimeoutMillis_=(value: Int): Unit

}

object ChannelConfig {

  def from(underlying: NettyChannelConfig): ChannelConfig =
    ChannelConfigImpl(underlying)

}