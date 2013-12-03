package org.sisioh.trinity.domain.mvc.filter

/**
 * シンプルな[[org.sisioh.trinity.domain.mvc.filter.Filter]]。
 *
 * @tparam Req リクエストの型
 * @tparam Rep レスポンスの型
 */
trait SimpleFilter[Req, Rep] extends Filter[Req, Rep, Req, Rep]
