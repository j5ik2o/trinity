# Trinity

[![Build Status](https://travis-ci.org/sisioh/trinity.png?branch=develop)](https://travis-ci.org/sisioh/trinity)

TrinityはScalaで記述できるFinagleベースの軽量MVCフレームワークです。

Trinity is a lightweight MVC framework based on Finagle, which can be described in Scala.

## コンセプト
- FinagleでMVCアプリケーションを実装するために不足している機能(特にVC関連)を補う。
- CoCを前提としない設計思想で、ドメイン駆動設計(モデル駆動設計)を支援する。

## 特徴
- Finagleサービスをコントローラ上のアクションとして記述できる。(RESTful APIサーバなどが簡単に実装できる)
  - Scalatraのように、コントローラ上のアクションにルーティング情報を記述できる。
  - Play2のように、コントローラの外部にルーティング情報を集約した記述ができる。
- Scalateなどのテンプレートエンジンと組み合わせてビューのあるアプリケーションも実装できる。

## 機能
### サポートする機能
- コントローラ上のアクションへのルーティング機能
- `com.twitter.util.Future`を前提としたブロッキングしない非同期型アクションの記述
  - `com.twitter.util.FuturePool`を使った同期型アクションも記述可能
  - `scala.concurrent.Future`のためのアダプタ機能もある
- Finagleリクエスト/レスポンスの拡張
  - マルチパート形式のファイルアップロード
  - JSON形式のレスポンス
  - ファイルリソース形式のレスポンス
- テンプレートエンジンとの連携
  - [Scalate](http://scalate.fusesource.org/)
  - [Velocity](http://velocity.apache.org/)
  - [FreeMarker](http://freemarker.org/)
  - [Thymeleaf](http://www.thymeleaf.org/)
- テスト機能
  - 単体テスト
  - 結合テスト
- JRebel
  - Trinity側で特別な実装はしていませんが、JRebelによるホットリローディングが利用できます。
  - 導入方法はこちらを参考にしてください。https://gist.github.com/j5ik2o/5660744

### サポートしない機能
- フォーム/バリデーション機能
- RDBMSやNoSQLなどの永続化機能
  - 特に制限はないので自由に組み合わせ可能。

## ライセンス
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
