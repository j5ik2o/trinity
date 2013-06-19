package org.sisioh.trinity.example

import org.sisioh.trinity.domain.controller.GlobalSetting
import org.sisioh.trinity.domain.http.{ResponseBuilder, Request}
import com.twitter.util.{FuturePool, Future}
import com.twitter.finagle.http.Response
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.application.TrinityApplication
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

class UnauthorizedException extends Exception

trait Example {

  val globalSettings = new GlobalSetting {
    def error(request: Request): Future[Response] = {
      request.error match {
        case Some(e: ArithmeticException) =>
          ResponseBuilder().withStatus(500).withPlain("whoops, divide by zero!").toFuture
        case Some(e: UnauthorizedException) =>
          ResponseBuilder().withStatus(401).withPlain("Not Authorized!").toFuture
        case Some(e) =>
          ResponseBuilder().withStatus(415).withPlain("Unsupported Media Type!").toFuture
        case _ =>
          ResponseBuilder().withStatus(500).withPlain("Something went wrong!").toFuture
      }
    }

    def notFound(request: Request): Future[Response] = {
      ResponseBuilder().withStatus(404).withPlain("not found yo").toFuture
    }

  }

  val config = Config()
  implicit val application = TrinityApplication(config, Some(globalSettings))

  // Thread Pool
  val threadPool = Executors.newFixedThreadPool(10)
  implicit val futurePool = FuturePool(threadPool)
  implicit val executor = ExecutionContext.fromExecutor(threadPool)

}
