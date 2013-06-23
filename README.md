trinity
=======

MVC framework by Finagle based

- FinagleをベースにしたMVCフレームワーク
- コントローラのアクションは基本的にFutureを返す前提。
  - com.twitter.util.Future, scala.concurrnet.Futureの両方が使える。
- Scalatraのようにコントローラにルート情報を記述する方法と、Play2のようにルート情報をまとめて記述する方法の、二通りが可能。
- テンプレートエンジンは、Scalate, Thymeleafに対応。
