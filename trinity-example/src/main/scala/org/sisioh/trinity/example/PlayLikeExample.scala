package org.sisioh.trinity.example

import org.jboss.netty.handler.codec.http.HttpMethod
import org.sisioh.trinity.domain.controller.AbstractController
import org.sisioh.trinity.domain.routing._
import scala.concurrent._

object PlayLikeExample extends App with ApplicationContext {

  object PlayLikeController extends AbstractController {

    /**
     * `com.twitter.util.FuturePool`で実現するアクション
     *
     * ブロッキングする処理でもスレッドプールが枯渇しなければ並行処理可能。
     *
     * @return
     */
    def index = FuturePoolAction {
      request =>
        responseBuilder.withOk.getTrinityResponse
    }

    /**
     * `com.twitter.util.Future`で実現するアクション
     *
     * ブロッキングする処理を書かないようにする。
     *
     * @return
     */
    def getUser(name: String) = FutureAction {
      request =>
        responseBuilder.withBody("name = " + name).toTrinityResponseFuture
    }

    /**
     * `scala.concurrent.Future`で実現するアクション
     * @return
     */
    def getGroup(name: String) = ScalaFutureAction {
      request => future {
        responseBuilder.withBody("group = " + name).getTrinityResponse
      }
    }

  }

  implicit val pathParser = new SinatraPathPatternParser()

  application.addRoute(HttpMethod.GET, "/", PlayLikeController, PlayLikeController.index)
  application.addRoute(Route(HttpMethod.GET, "/user/:name", PlayLikeController, FutureAction {
    request =>
      PlayLikeController.getUser(request.routeParams("name"))(request)
  }))
  application.addRoute(HttpMethod.GET, "/group/:name", PlayLikeController) {
    request =>
      PlayLikeController.getGroup(request.routeParams("name"))(request)
  }

  application.registerController(PlayLikeController)
  application.start()
}
