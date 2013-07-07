# Trinity

[![Build Status](https://travis-ci.org/sisioh/trinity.png?branch=develop)](https://travis-ci.org/sisioh/trinity)

Trinity is a lightweight MVC framework based on Finagle, which can be described in Scala.

## Concepts
- We provide functions about MVC which does not supported by finagle.
- We support Domain Driven-Design by non CoC(Convention over Configuration).

## Features
- You can describe Actions over a Controller as a Finagle Service.
  - A routing information can described to action methods, like Scalatra.
  - Or, The Routing informations can be aggregated on the outside of the controller, like Play2.
- You can use Template Engine (such as Scalatra) with Trinity.

## Functions
### Supported Functions
- Routing request to action
- `com.twitter.util.Future`を前提としたブロッキングしない非同期型アクションの記述
  - `com.twitter.util.FuturePool`を使った同期型アクションも記述可能
  - `scala.concurrent.Future`のためのアダプタ機能もある
- Finagle's Request/Response Enhance
  - マルチパート形式のファイルアップロード
  - JSON形式のレスポンス
  - ファイルリソース形式のレスポンス
- Binding to Template Engine
  - [Scalate](http://scalate.fusesource.org/)
  - [Velocity](http://velocity.apache.org/)
  - [FreeMarker](http://freemarker.org/)
  - [Thymeleaf](http://www.thymeleaf.org/)
- Testing
  - Unit Testing
  - Integration Testing
- JRebel support
  - Trinity側で特別な実装はしていませんが、JRebelによるホットリローディングが利用できます。
  - 導入方法はこちらを参考にしてください。https://gist.github.com/j5ik2o/5660744

### Unsupported Functions
- Functions for Form, Validation 
- Functions for Persitence(sush as RDBMS/NoSQL)

## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
