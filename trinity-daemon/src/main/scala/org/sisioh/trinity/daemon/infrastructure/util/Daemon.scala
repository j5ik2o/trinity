package org.sisioh.trinity.daemon.infrastructure.util

import org.apache.commons.daemon.{DaemonContext, Daemon => CommonsDaemon}
import org.sisioh.scala.toolbox.LoggingEx
import org.sisioh.trinity._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait Daemon extends CommonsDaemon with LoggingEx {

  trait DaemonApplication extends Application with Bootstrap

  protected def createApplication(args: Array[String]): DaemonApplication

  @volatile
  private var application: DaemonApplication = _

  @volatile
  private var context: DaemonContext = _

  def init(context: DaemonContext) {
    withInfoScope("Daemon#init") {
      application = createApplication(context.getArguments)
      this.context = context
    }
  }

  def start() {
    withInfoScope("Daemon#start") {
      Await.result(application.start(), Duration.Inf)
    }
  }

  def stop() {
    withInfoScope("Daemon#stop") {
      Await.result(application.stop(), Duration.Inf)
    }
  }

  def destroy() {
    withInfoScope("Daemon#destroy") {
    }
  }

}
