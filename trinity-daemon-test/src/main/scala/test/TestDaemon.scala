package test

import org.sisioh.trinity._
import org.sisioh.trinity.daemon._

/**
 * アプリケーション。
 *
 * @param environment
 */
case class TestApplication(environment: Environment.Value)
  extends Application with Bootstrap with ScalatraLikeApplicationSupport {

  get("/test") {
    request =>
      responseBuilder.withContent("test").toFuture
  }

}

/**
 * コンソールアプリケーション用エントリポイント。
 */
object TestApplication extends App {

  TestApplication(Environment.Development).startWithAwait()

}

/**
 * デーモン用エントリポイント。
 */
class TestDaemon extends Daemon {

  protected def createApplication(args: Array[String]): Application = {
    TestApplication(Environment.Product)
  }

}
