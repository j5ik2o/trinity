package org.sisioh.trinity.domain.io.http

import org.jboss.netty.handler.codec.http.HttpMethod
import scala.language.implicitConversions

/**
 * Represents the enumeration for HTTP Methods.
 */
object Methods extends Enumeration {

  /**
   * OPTION
   */
  val Options,

  /**
   * GET
   */
  Get,

  /**
   * HEAD
   */
  Head,

  /**
   * POST
   */
  Post,

  /**
   * PUT
   */
  Put,

  /**
   * PATCH
   */
  Patch,

  /**
   * DELETE
   */
  Delete,

  /**
   * TRACE
   */
  Trace,

  /**
   * CONNECT
   */
  Connect = Value

  private[domain] implicit def toNetty(value: Methods.Value): HttpMethod = {
    HttpMethod.valueOf(value.toString.toUpperCase)
  }

  private[domain] implicit def toTrintiy(value: HttpMethod): Methods.Value = {
    val strings = value.toString.splitAt(1)
    val enumName = strings._1.toUpperCase + strings._2.toLowerCase
    Methods.withName(enumName)
  }

}
