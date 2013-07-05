# Trinity

[![Build Status](https://travis-ci.org/sisioh/trinity.png?branch=develop)](https://travis-ci.org/sisioh/trinity)

Trinityは `Finagle`ベースの軽量MVCフレームワークです。

## コンセプト
- FinagleでMVCアプリケーションを実装するために不足している機能を補う。
- CoCを前提としない設計思想で、ドメイン駆動設計(モデル駆動設計)を支援する。

## 特徴
- `Finagle`サービスをコントローラ上のアクションとして記述できる。
- `Scalate`などのテンプレートエンジンと組み合わせて利用できる。

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

### サポートしない機能
- フォーム機能
- バリデーション機能
- RDBMSやNoSQLなどの永続化機能

## ライセンス
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
