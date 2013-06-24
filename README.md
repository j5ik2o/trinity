trinity
=======

MVC framework by Finagle based

- FinagleをベースにしたMVCフレームワーク
- コントローラのアクションは基本的にFutureを返す前提。
  - com.twitter.util.Future, scala.concurrnet.Futureの両方が使える。
  - FuturePoolを使ったブロッキング前提のアクションも記述可能。
- Scalatraのようにコントローラにルート情報を記述する方法と、Play2のようにルート情報をまとめて記述する方法の、二通りが可能。

```scala
object ScalatraLikeController extends SimpleController {

    get("/user/:username") {
      request =>
        val username = request.routeParams.getOrElse("username", "default_user")
        responseBuilder.withPlain("hello " + username).toFuture
    }

}
```

```scala
object PlayLikeController extends AbstractController {

    // Play2のようなアクション。
    // ブロッキングする処理を書いてもブロッキングしない。
    def index = FuturePoolAction {
      request =>
        responseBuilder.withOk.getResultByAwait
    }

    // com.twitter.util.Future前提のアクション
    def getUser(name: String) = FutureAction {
      request =>
        responseBuilder.withBody("name = " + name).toFuture
    }

    // scala.concurrent.Futureにアダプトするアクション
    def getGroup(name: String) = ScalaFutureAction {
      request => future {
        responseBuilder.withBody("group = " + name).getResultByAwait
      }
    }

}

// ルーティング情報をまとめることができる。
application.addRoute(HttpMethod.GET, "/", PlayLikeController, PlayLikeController.index)
application.addRoute(HttpMethod.GET, "/user/:name", PlayLikeController) {
    request =>
      PlayLikeController.getUser(request.routeParams("name"))(request)
}
application.addRoute(HttpMethod.GET, "/group/:name", PlayLikeController) {
    request =>
      PlayLikeController.getGroup(request.routeParams("name"))(request)
}
```
- テンプレートエンジンは、Scalate, Thymeleaf, Velocity, FreeMarkerに対応。
