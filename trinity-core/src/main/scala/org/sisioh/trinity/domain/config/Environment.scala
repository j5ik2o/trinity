package org.sisioh.trinity.domain.config

/**
 * 環境を表す列挙型。
 */
object Environment extends Enumeration {
  /**
   * 本番環境
   */
  val Product,

  /**
   * 開発環境
   */
  Development = Value
}
