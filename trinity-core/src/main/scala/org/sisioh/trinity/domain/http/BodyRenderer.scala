package org.sisioh.trinity.domain.http

import com.twitter.util.Future

/**
 * レスポンスのボディをレンダリングするためのトレイト。
 */
trait BodyRenderer {

  /**
   * ボディをレンダリングする。
   * @return
   */
  def render: Future[String]

}
