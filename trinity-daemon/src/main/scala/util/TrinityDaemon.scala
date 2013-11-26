package org.sisioh.trinity.infrastructure.util

import org.apache.commons.daemon.{DaemonContext, Daemon}
import org.sisioh.scala.toolbox.LoggingEx
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import org.sisioh.trinity._

trait TrinityDaemon extends Daemon with LoggingEx {

  trait DaemonApplication extends Application with Bootstrap

  protected def createApplication(args: Array[String]): DaemonApplication

  @volatile
  private var application: DaemonApplication = _

  @volatile
  private var context: DaemonContext = _

  def init(context: DaemonContext) {
    withInfoScope("TrinityDaemon#init") {
      application = createApplication(context.getArguments)
      this.context = context
    }
  }

  def start() {
    withInfoScope("TrinityDaemon#start") {
      Await.result(application.start(), Duration.Inf)
    }
  }

  def stop() {
    withInfoScope("TrinityDaemon#stop") {
      Await.result(application.stop(), Duration.Inf)
    }
  }

  def destroy() {
    withInfoScope("TrinityDaemon#destroy") {
    }
  }

}
