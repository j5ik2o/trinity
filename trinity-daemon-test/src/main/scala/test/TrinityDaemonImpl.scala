package test

import org.sisioh.trinity._
import org.sisioh.trinity.infrastructure.util.TrinityDaemon
import org.sisioh.trinity.domain.mvc.application.ScalatraLikeApplicationSupport

class TrinityDaemonImpl extends TrinityDaemon {

  case class MyApplication(environment: Environment.Value)
    extends DaemonApplication with ScalatraLikeApplicationSupport {
    get("/test") {
      request =>
        responseBuilder.withContent("").toFuture
    }
  }

  protected def createApplication(args: Array[String]): DaemonApplication = {
    val env = if (args.size > 1 && args(1) == Environment.Development.toString)
      Environment.Development
    else
      Environment.Product
    MyApplication(env)
  }

}
