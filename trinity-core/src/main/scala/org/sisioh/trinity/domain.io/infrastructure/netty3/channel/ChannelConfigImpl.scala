package org.sisioh.trinity.domain.io.infrastructure.netty3.channel

import java.util.{Map => JMap}
import org.jboss.netty.channel.{ChannelConfig => NettyChannelConfig, ChannelPipelineFactory}
import org.sisioh.trinity.domain.io.buffer.ChannelBufferFactory
import org.sisioh.trinity.domain.io.channel.ChannelConfig
import org.sisioh.trinity.domain.io.infrastructure.netty3.buffer.ChannelBuffersImpl
import scala.collection.JavaConverters._

case class ChannelConfigImpl(underlying: NettyChannelConfig) extends ChannelConfig {

  import ChannelBuffersImpl._

  def setOptions(options: Map[String, Any]) {
    underlying.setOptions(options.asJava.asInstanceOf[JMap[String, AnyRef]])
  }

  def setOption(name: String, value: Any) {
    underlying.setOption(name, value)
  }

  def bufferFactory: ChannelBufferFactory =
    ChannelBufferFactory.from(underlying.getBufferFactory)

  def bufferFactory_=(value: ChannelBufferFactory) {
    underlying.setBufferFactory(value)
  }

  def pipelineFactory: ChannelPipelineFactory = ???

  def pipelineFactory_=(value: ChannelPipelineFactory) {}

  def connectTimeoutMillis: Int = underlying.getConnectTimeoutMillis

  def connectTimeoutMillis_=(value: Int) {
    underlying.setConnectTimeoutMillis(value)
  }
}
