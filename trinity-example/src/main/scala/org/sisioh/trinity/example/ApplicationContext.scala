package org.sisioh.trinity.example

import org.sisioh.trinity.domain.controller.GlobalSettings
import org.sisioh.trinity.domain.http.{ResponseBuilder, Request}
import com.twitter.util.{FuturePool, Future}
import com.twitter.finagle.http.Response
import org.sisioh.trinity.domain.config.Config
import org.sisioh.trinity.application.TrinityApplication
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import org.sisioh.trinity.view.scalate.ScalateEngineContext
import org.sisioh.trinity.view.thymeleaf.ThymeleafEngineContext
import org.sisioh.trinity.view.velocity.VelocityEngineContext
import org.sisioh.trinity.view.freemarker.FreeMarkerEngineContext
import org.sisioh.trinity.view.smarty4j.Smarty4JEngineContext

class UnauthorizedException extends Exception

trait ApplicationContext {

  val globalSettings = new GlobalSettings {
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

  implicit val config = Config()
  implicit val application = TrinityApplication(config, Some(globalSettings))

  // Thread Pool
  val threadPool = Executors.newFixedThreadPool(10)
  implicit val futurePool = FuturePool(threadPool)
  implicit val executor = ExecutionContext.fromExecutor(threadPool)

  implicit val scalate = ScalateEngineContext()
  implicit val thymeleaf = ThymeleafEngineContext()
  implicit val velocity = VelocityEngineContext()
  implicit val freemarker = FreeMarkerEngineContext(getClass)
  implicit val smart4j = Smarty4JEngineContext()
}
