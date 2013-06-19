package org.sisioh.trinity.example

import org.sisioh.trinity.domain.routing._
import scala.concurrent._
import org.sisioh.trinity.domain.controller.AbstractController
import org.jboss.netty.handler.codec.http.HttpMethod

object PlayLikeExample extends App with Example {

  object PlayLikeController extends AbstractController {

    /**
     * `com.twitter.util.FuturePool`で実現するアクション
     * @return
     */
    def index = FuturePoolAction {
      request =>
        responseBuilder.withOk.build
    }

    /**
     * `com.twitter.util.Future`で実現するアクション
     * @return
     */
    def getUser = FutureAction {
      request =>
        val name = request.routeParams("name")
        responseBuilder.withBody("name = " + name).toFuture
    }

    /**
     * `scala.concurrent.Future`で実現するアクション
     * @return
     */
    def getGroup = ScalaFutureAction {
      request => future {
        val name = request.routeParams("name")
        responseBuilder.withBody("group = " + name).build
      }
    }

  }

  implicit val pathParser = new SinatraPathPatternParser()

  application.addRoute(HttpMethod.GET, "/", PlayLikeController.index)
  application.addRoute(HttpMethod.GET, "/user/:name", PlayLikeController.getUser)
  application.addRoute(HttpMethod.GET, "/group/:name", PlayLikeController.getGroup)

  application.registerController(PlayLikeController)
  application.start()
}
