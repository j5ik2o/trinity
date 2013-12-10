package org.sisioh.trinity.example

import org.json4s._
import org.sisioh.trinity._

object ScalatraLikeControllerApplication extends ConsoleApplication with ScalatraLikeApplicationSupport {

  get("/hello") {
    request =>
      responseBuilder.withTextPlain("Hello World!!").toFuture
  }

  get("/json") {
    request =>
      val jValue = JObject(
        JField("name", JString("value"))
      )
      responseBuilder.withJson(jValue).toFuture
  }

  get("/user/:userId") {
    request =>
      responseBuilder.withTextPlain("userId = " + request.routeParams("userId")).toFuture
  }

  get( """/group/(.*)""".r, Seq("name")) {
    request =>
      responseBuilder.withTextPlain("name = " + request.routeParams("name")).toFuture
  }

  startWithAwait()

}
