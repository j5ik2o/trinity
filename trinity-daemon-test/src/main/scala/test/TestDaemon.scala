package test

import org.sisioh.trinity._
import org.sisioh.trinity.daemon._

class TestDaemon extends Daemon {

  case class TestDaemonApplication(environment: Environment.Value)
    extends DaemonApplication with ScalatraLikeApplicationSupport {
    get("/test") {
      request =>
        responseBuilder.withContent("test").toFuture
    }
  }

  protected def createApplication(args: Array[String]): DaemonApplication = {
    val env = if (args.size > 1 && args(1) == Environment.Development.toString)
      Environment.Development
    else
      Environment.Product
    TestDaemonApplication(env)
  }

}
